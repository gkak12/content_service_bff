package com.service.user.service.impl

import com.service.common.enums.ErrorCodeEnums
import com.service.common.enums.JwtEnums
import com.service.common.exception.ContentException
import com.service.common.response.JwtResponse
import com.service.user.model.dto.UserDto
import com.service.user.security.JwtUtil
import com.service.user.service.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Service
class JwtServiceImpl(
    private val jwtUtil: JwtUtil,
    @Value("\${jwt.refresh-token-expiration}")
    private val validityRefreshTime: Long
): JwtService{

    override fun refreshToken(exchange: ServerWebExchange): Mono<JwtResponse> {
        val request = exchange.request
        val response = exchange.response

        val refreshToken = request.cookies["refreshToken"]?.firstOrNull()?.value

        if(refreshToken.isNullOrBlank()){
            return Mono.error(ContentException(ErrorCodeEnums.NOT_FOUND, "Refresh token is missing."))
        }

        val id = jwtUtil.getAdminId(refreshToken)
        val role = jwtUtil.getAdminRole(refreshToken)
        val valid = jwtUtil.validateToken(refreshToken, id, role)

        if(!valid){
            throw ContentException(ErrorCodeEnums.VALIDATION_CHECK, "Refresh token is not valid.")
        }

        val userDto = UserDto(
            userId = id,
            userLoginType = role
        )

        val refreshTokenCookie = ResponseCookie.from("refreshToken", jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, userDto))
            .httpOnly(true)
//            .secure(true)   // https
            .path("/")
            .maxAge(validityRefreshTime/1000)
            .sameSite("Strict")
            .build()

        response.addCookie(refreshTokenCookie)

        val jwtReponse = JwtResponse(
            accessToken = jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, userDto),
            refreshToken = "",
            msg = "$id: JWT 토큰 재발급 했습니다."
        )

        return Mono.just(jwtReponse)
    }
}
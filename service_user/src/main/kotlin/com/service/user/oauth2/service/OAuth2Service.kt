package com.service.user.oauth2.service

import com.service.common.enums.JwtEnums
import com.service.common.response.JwtResponse
import com.service.user.model.dto.UserDto
import com.service.user.security.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Base64

@Service
class OAuth2Service(
    private val jwtUtil: JwtUtil,
    @Value("\${jwt.refresh-token-expiration}")
    private val validityRefreshTime: Long
){

    fun createToken(encodedId: String, encodedName: String, response: ServerHttpResponse): Mono<JwtResponse> {
        val userId = String(Base64.getDecoder().decode(encodedId))
        val userName = String(Base64.getDecoder().decode(encodedName))
        val userDto = UserDto(
            userId = userId,
            userName = userName
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
            msg = "$userId: OAuth2 로그인 성공했습니다."
        )

        return Mono.just(jwtReponse)
    }
}
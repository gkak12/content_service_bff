package com.service.user.oauth2.service

import com.service.common.enums.JwtEnums
import com.service.common.enums.LoginEnums
import com.service.common.response.JwtResponse
import com.service.user.model.dto.UserDto
import com.service.user.security.JwtUtil
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class OAuth2Service(
    private val jwtUtil: JwtUtil
){

    fun createToken(encodedId: String, response: ServerHttpResponse): Mono<JwtResponse> {
        val userId = String(Base64.getDecoder().decode(encodedId))
        val userDto = UserDto(
            userId = userId,
            userType = LoginEnums.NAVER_OAUTH2.value
        )

        val refreshToken = jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, userDto)
        val refreshTokenCookie = jwtUtil.createRefreshTokenCookie(refreshToken)

        response.addCookie(refreshTokenCookie)

        val jwtReponse = JwtResponse(
            accessToken = jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, userDto),
            refreshToken = "",
            msg = "$userId: OAuth2 로그인 성공했습니다."
        )

        return Mono.just(jwtReponse)
    }
}
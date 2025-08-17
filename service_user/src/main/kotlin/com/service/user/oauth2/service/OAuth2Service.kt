package com.service.user.oauth2.service

import com.service.common.enums.JwtEnums
import com.service.common.response.JwtResponse
import com.service.user.model.dto.UserDto
import com.service.user.security.JwtUtil
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Base64

@Service
class OAuth2Service(
    private val jwtUtil: JwtUtil
){

    fun createToken(encodedId: String, encodedName: String): Mono<JwtResponse> {
        val userId = String(Base64.getDecoder().decode(encodedId))
        val userName = String(Base64.getDecoder().decode(encodedName))
        val userDto = UserDto(
            userId = userId,
            userName = userName
        )

        val jwtReponse = JwtResponse(
            jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, userDto),
            jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, userDto),
            "$userId: OAuth2 로그인 성공했습니다."
        )

        return Mono.just(jwtReponse)
    }
}
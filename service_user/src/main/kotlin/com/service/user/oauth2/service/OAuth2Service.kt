package com.service.user.oauth2.service

import com.service.account.GrpcUserProtoDto
import com.service.account.GrpcUserRequest
import com.service.common.enums.JwtEnums
import com.service.common.enums.LoginEnums
import com.service.common.response.JwtResponse
import com.service.grpc.service.GrpcClientUserService
import com.service.user.model.mapper.UserMapper
import com.service.user.security.JwtUtil
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class OAuth2Service(
    private val jwtUtil: JwtUtil,
    private val userMapper: UserMapper,
    private val grpcClientUserService: GrpcClientUserService
) {

    fun createToken(encodedId: String): Mono<JwtResponse>{
        val userId = String(Base64.getDecoder().decode(encodedId))

        return grpcClientUserService.findUserById(
                GrpcUserRequest.newBuilder()
                    .setUserId(userId)
                    .build()
            )
            .switchIfEmpty(Mono.error(NoSuchElementException("등록되지 않은 계정입니다.")))
            .map { response ->
                val userDto = userMapper.toDto(response.dtoList.get(0))

                JwtResponse(
                    jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, userDto),
                    jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, userDto),
                    "$userId: OAuth2 로그인 성공했습니다."
                )
            }
    }

    fun createUser(email: String, name: String): Mono<Void>{
        return grpcClientUserService.createUser(GrpcUserProtoDto.newBuilder()
            .setUserId(email)
            .setUserName(name)
            .setUserLoginType(LoginEnums.NAVER_OAUTH2.value)
            .build()
        ).then()
    }
}
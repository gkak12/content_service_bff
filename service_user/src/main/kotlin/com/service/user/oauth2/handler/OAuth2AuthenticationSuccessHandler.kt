package com.service.user.oauth2.handler

import com.service.account.GrpcUserProtoDto
import com.service.common.enums.LoginEnums
import com.service.grpc.service.GrpcClientUserService
import com.service.user.model.mapper.UserMapper
import com.service.user.security.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*

@Component
class OAuth2AuthenticationSuccessHandler(
    private val userMapper: UserMapper,
    private val grpcClientUserService: GrpcClientUserService
): ServerAuthenticationSuccessHandler {

    private val logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler::class.java)

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication
    ): Mono<Void> {
        logger.info("SUCCESS HANDLER TRIGGERED")

        val principal = authentication.principal as OAuth2User
        val userEmail = principal.getAttribute<String>(LoginEnums.NAVER_OAUTH2_EMAIL.value)
        val userName = principal.getAttribute<String>(LoginEnums.NAVER_OAUTH2_NAME.value)

        logger.info("email: ${userEmail}, name: ${userName}")

        // 비동기적으로 사용자 정보를 저장한 후 리다이렉트
        return grpcClientUserService.loginUser(
                GrpcUserProtoDto.newBuilder()
                    .setUserId(userEmail)
                    .setUserName(userName)
                    .setUserLoginType(LoginEnums.NAVER_OAUTH2.value)
                    .build()
            )
            .flatMap { response ->
                val userDto = userMapper.toDto(response.dtoList[0])
                val exchange = webFilterExchange.exchange
                val responseObj = exchange.response

                // redirect
                var userId = Base64.getEncoder().encodeToString(userDto.userId.toByteArray())
                val uri = "/api/user/oauth2/me/${userId}"
                responseObj.statusCode = HttpStatus.FOUND
                responseObj.headers.location = URI.create(uri)

                Mono.empty()
            }
    }
}

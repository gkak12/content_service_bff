package com.service.user.oauth2.handler

import com.service.account.GrpcUserProtoDto
import com.service.common.enums.LoginEnums
import com.service.common.response.JwtResponse
import com.service.grpc.service.GrpcClientUserService
import com.service.user.model.mapper.UserMapper
import com.service.user.security.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.*

@Component
class OAuth2AuthenticationSuccessHandler(
    private val jwtUtil: JwtUtil,
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
                val accessToken = jwtUtil.createToken("access", userDto)
                val refreshToken = jwtUtil.createToken("refresh", userDto)
                val jwtResponse = JwtResponse(accessToken, refreshToken, "${userDto.userId}: OAuth2 로그인 성공했습니다.")

                // 응답 객체에 직접 JSON 작성
                val exchange = webFilterExchange.exchange
                val responseObj = exchange.response
                val bufferFactory = responseObj.bufferFactory()

                val objectMapper = com.fasterxml.jackson.databind.ObjectMapper()
                val bytes = objectMapper.writeValueAsBytes(jwtResponse)
                val buffer = bufferFactory.wrap(bytes)

                responseObj.headers.contentType = org.springframework.http.MediaType.APPLICATION_JSON
                responseObj.statusCode = org.springframework.http.HttpStatus.OK

                responseObj.writeWith(Mono.just(buffer))
            }
    }
}

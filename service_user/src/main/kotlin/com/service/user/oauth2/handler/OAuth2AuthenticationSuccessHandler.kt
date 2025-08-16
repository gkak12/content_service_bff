package com.service.user.oauth2.handler

import com.service.common.enums.LoginEnums
import com.service.user.oauth2.service.OAuth2Service
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
    private val oAuth2Service: OAuth2Service
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

        logger.info("email: ${userEmail}")

        // 비동기적으로 사용자 정보를 저장한 후 리다이렉트
        return oAuth2Service.loginUser(userEmail, userName)
            .then(redirectToUserInfo(webFilterExchange.exchange, userEmail))
    }

    private fun redirectToUserInfo(exchange: ServerWebExchange, userEmail: String): Mono<Void> {
        val encodedEmail = Base64.getEncoder().encodeToString(userEmail.toByteArray())
        val location = "/api/user/oauth2/me?id=$encodedEmail"

        val response = exchange.response
        response.statusCode = org.springframework.http.HttpStatus.FOUND // 302 Redirect
        response.headers.location = java.net.URI.create(location)

        return response.setComplete()
    }
}

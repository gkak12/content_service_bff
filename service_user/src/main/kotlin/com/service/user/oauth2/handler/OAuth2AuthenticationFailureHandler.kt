package com.service.user.oauth2.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Component
class OAuth2AuthenticationFailureHandler : ServerAuthenticationFailureHandler {

    private val logger = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler::class.java)

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange,
        exception: AuthenticationException
    ): Mono<Void> {
        val response = webFilterExchange.exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        response.headers.contentType = MediaType.APPLICATION_JSON
        logger.info("response: ${response}")

        val bufferFactory = response.bufferFactory()
        val errorJson = """{ "error": "${exception.message ?: "Authentication failed"}" }"""
        val dataBuffer = bufferFactory.wrap(errorJson.toByteArray(StandardCharsets.UTF_8))

        return response.writeWith(Mono.just(dataBuffer))
    }
}

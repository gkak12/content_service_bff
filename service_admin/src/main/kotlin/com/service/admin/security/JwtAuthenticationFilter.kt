package com.service.admin.security

import com.service.common.enums.JwtEnums
import com.service.common.util.RedisUtil
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val redisUtil: RedisUtil
): WebFilter {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val uri = exchange.request.uri.path

        if (uri.startsWith("/users/login") || uri.startsWith("/users/signup") || uri.startsWith("/oauth2/me")) {
            return chain.filter(exchange)
        }

        return Mono.justOrEmpty(getAccessTokenFromRequest(exchange))
            .flatMap { accessToken ->
                val userId = jwtUtil.getUsername(accessToken ?: throw IllegalArgumentException("Token is missing"))

                // 삭제된 계정인지 확인
                val userTokenKey = "$userId${JwtEnums.TOKEN_KEY.value}"
                return@flatMap Mono.justOrEmpty(redisUtil.getRefreshToken(userTokenKey))
                    .switchIfEmpty(Mono.error(IllegalStateException("삭제된 계정입니다.")))
                    .flatMap {
                        if (jwtUtil.validateToken(accessToken, userId)) {
                            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                            val authentication = UsernamePasswordAuthenticationToken(userId, null, authorities)

                            // 인증 정보 설정
                            return@flatMap chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                        } else {
                            Mono.error(IllegalStateException("Invalid token"))
                        }
                    }
            }
            .onErrorResume { e ->
                log.info("JWT Filter Exception: ${e.message}")
                exchange.response.statusCode = org.springframework.http.HttpStatus.UNAUTHORIZED
                Mono.empty() // 인증 오류 후 empty response 반환
            }
    }

    private fun getAccessTokenFromRequest(exchange: ServerWebExchange): String? {
        val header = exchange.request.headers.getFirst("Authorization")
        return if (header != null && header.startsWith("Bearer ")) {
            header.substring(7)
        } else null
    }
}
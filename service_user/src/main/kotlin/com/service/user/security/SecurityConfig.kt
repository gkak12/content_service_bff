package com.service.user.security

import com.service.common.util.RedisUtil
import com.service.user.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.service.user.oauth2.handler.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    private val successHandler: OAuth2AuthenticationSuccessHandler,
    private val failureHandler: OAuth2AuthenticationFailureHandler
){

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun jwtAuthenticationFilter(jwtUtil: JwtUtil, redisUtil: RedisUtil): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtUtil, redisUtil)
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .cors { }  // default 설정
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange {
                it.pathMatchers(*UserAuthWhitelist.paths).permitAll()
                it.anyExchange().authenticated()
            }
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .oauth2Login { oauth2 ->
                oauth2.authenticationSuccessHandler(successHandler)
                oauth2.authenticationFailureHandler(failureHandler)
            }
            .build()
    }
}

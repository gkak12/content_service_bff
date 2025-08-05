package com.service.admin.security

import com.service.common.enums.RoleEnums
import com.service.common.util.RedisUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun jwtAuthenticationFilter(jwtUtil: JwtUtil, redisUtil: RedisUtil): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtUtil, redisUtil)
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityWebFilterChain {
        http
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange {
                it.pathMatchers(*AdminAuthWhitelist.paths).permitAll()
                    .pathMatchers("/api/admin/**").hasAnyAuthority(RoleEnums.ROLE_SUPER_ADMIN.value, RoleEnums.ROLE_ADMIN.value)
                    .anyExchange().authenticated()
            }
            .httpBasic(Customizer.withDefaults())

        return http.build()
    }
}

package com.service.user.service

import com.service.common.response.JwtResponse
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface JwtService {

    fun refreshToken(exchange: ServerWebExchange): Mono<JwtResponse>
}
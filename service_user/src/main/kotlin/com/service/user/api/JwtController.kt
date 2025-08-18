package com.service.user.api

import com.service.common.response.JwtResponse
import com.service.user.service.JwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user/jwt")
class JwtController(
    private val jwtService: JwtService
){

    @GetMapping("/refresh")
    fun refreshToken(exchange: ServerWebExchange): Mono<JwtResponse>{
        return jwtService.refreshToken(exchange)
    }
}
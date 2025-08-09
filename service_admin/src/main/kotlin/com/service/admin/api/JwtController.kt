package com.service.admin.api

import com.service.admin.model.request.RequestJwtTokenDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.service.JwtService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/jwt")
class JwtController(
    private val jwtService: JwtService
){

    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody jwtToken: RequestJwtTokenDto): Mono<ResponseJwtTokenDto>{
        return jwtService.refresh(jwtToken)
    }
}
package com.service.admin.service

import com.service.admin.model.request.RequestJwtTokenDto
import com.service.admin.model.response.ResponseJwtTokenDto
import reactor.core.publisher.Mono

interface JwtService {

    fun refresh(jwtToken: RequestJwtTokenDto): Mono<ResponseJwtTokenDto>
}
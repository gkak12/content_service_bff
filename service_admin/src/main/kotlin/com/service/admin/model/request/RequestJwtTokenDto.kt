package com.service.admin.model.request

import jakarta.validation.constraints.NotBlank

data class RequestJwtTokenDto(

    @field:NotBlank
    val refreshToken: String
)
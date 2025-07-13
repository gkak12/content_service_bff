package com.service.admin.model.request

import jakarta.validation.constraints.NotBlank

data class RequestUserLoginDto(

    @field:NotBlank
    val id: String,

    @field:NotBlank
    val password: String
)

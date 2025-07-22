package com.service.admin.model.request

import jakarta.validation.constraints.NotBlank

data class RequestAdminSignupDto(

    @field:NotBlank
    val id: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val email: String
)
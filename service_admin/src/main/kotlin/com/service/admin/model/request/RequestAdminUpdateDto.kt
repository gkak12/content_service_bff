package com.service.admin.model.request

import jakarta.validation.constraints.NotBlank

data class RequestAdminUpdateDto (

    @field:NotBlank
    val adminId: String,

    @field:NotBlank
    val adminName: String,

    @field:NotBlank
    val email: String
)
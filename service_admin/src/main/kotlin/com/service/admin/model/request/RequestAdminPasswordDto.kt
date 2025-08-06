package com.service.admin.model.request

import jakarta.validation.constraints.NotBlank

class RequestAdminPasswordDto (

    @field:NotBlank
    val id: String,

    @field:NotBlank
    val password: String
)
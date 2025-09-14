package com.service.admin.model.request

import jakarta.validation.constraints.NotBlank

data class RequestPointPagingDto (

    @field:NotBlank
    val pageNumber: Int,

    @field:NotBlank
    val pageRow: Int,
)
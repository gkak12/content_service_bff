package com.service.admin.model.dto

data class PointDto (
    val pointSeq: Long = 0L,
    val userSeq: Long = 0L,
    val typeSeq: Long = 0L,
    val orderSeq: Long = 0L,
    val amount: Long = 0L,
    val content: String = "",
    val creationDateTime: String = "",
    val expirationDateTime: String = ""
)
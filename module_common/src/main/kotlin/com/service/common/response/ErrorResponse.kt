package com.service.common.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.service.common.enums.ErrorCodeEnum
import com.service.common.exception.ContentException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val value: String,
    val message: String,
    val detailMessage: List<FieldErrorMessage>? = null
) {
    constructor(e: ContentException) : this(
        value = e.value,
        message = e.message ?: "",
        detailMessage = null
    )

    constructor(errorType: ErrorCodeEnum, message: String) : this(
        value = errorType.value,
        message = message,
        detailMessage = null
    )

    constructor(errorType: String, message: String) : this(
        value = errorType,
        message = message,
        detailMessage = null
    )

    constructor(errorType: ErrorCodeEnum, message: String, detailMessage: List<FieldErrorMessage>) : this(
        value = errorType.value,
        message = message,
        detailMessage = detailMessage
    )

    data class FieldErrorMessage(
        val field: String,
        val errorMessage: String
    )
}

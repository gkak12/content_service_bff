package com.service.common.response

import com.service.common.enums.ErrorCodeEnum
import org.springframework.validation.BindingResult

data class ValidationErrorResponse(
    val value: String = ErrorCodeEnum.VALIDATION_CHECK.value,
    val msg: String = ErrorCodeEnum.VALIDATION_CHECK.msg,
    val code: Int = ErrorCodeEnum.VALIDATION_CHECK.status,
    val fieldErrorMessages: List<FieldErrorMessage>? = null
) {
    constructor(bindingResult: BindingResult?) : this(
        fieldErrorMessages = bindingResult
            ?.fieldErrors
            ?.takeIf { it.isNotEmpty() }
            ?.map { FieldErrorMessage(it.field, it.defaultMessage ?: "") }
    )

    data class FieldErrorMessage(
        val field: String,
        val errorMessage: String
    )
}
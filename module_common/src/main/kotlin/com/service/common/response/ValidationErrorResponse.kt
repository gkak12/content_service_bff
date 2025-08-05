package com.service.common.response

import com.service.common.enums.ErrorCodeEnums
import org.springframework.validation.BindingResult

data class ValidationErrorResponse(
    val value: String = ErrorCodeEnums.VALIDATION_CHECK.value,
    val msg: String = ErrorCodeEnums.VALIDATION_CHECK.msg,
    val code: Int = ErrorCodeEnums.VALIDATION_CHECK.status,
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
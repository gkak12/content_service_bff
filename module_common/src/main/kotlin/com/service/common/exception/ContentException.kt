package com.service.common.exception

import com.service.common.enums.ErrorCodeEnum

class ContentException : RuntimeException {
    val value: String
    override val message: String
    val errorStatus: Int

    constructor(errorCodeEnum: ErrorCodeEnum) : super(errorCodeEnum.msg) {
        this.value = errorCodeEnum.value
        this.message = errorCodeEnum.msg
        this.errorStatus = errorCodeEnum.status
    }

    constructor(errorCodeEnum: ErrorCodeEnum, message: String) : super(message) {
        this.value = errorCodeEnum.value
        this.message = message
        this.errorStatus = errorCodeEnum.status
    }

    constructor(status: String, message: String, errorStatus: Int) : super(message) {
        this.value = status
        this.message = message
        this.errorStatus = errorStatus
    }
}
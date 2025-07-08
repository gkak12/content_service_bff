package com.service.common.exception

import com.service.common.enums.ErrorCodeEnum

class ContentException : RuntimeException {
    val status: String
    override val message: String
    val errorStatus: Int

    constructor(errorCodeEnum: ErrorCodeEnum) : super(errorCodeEnum.msg) {
        this.status = errorCodeEnum.value
        this.message = errorCodeEnum.msg
        this.errorStatus = errorCodeEnum.code
    }

    constructor(errorCodeEnum: ErrorCodeEnum, message: String) : super(message) {
        this.status = errorCodeEnum.value
        this.message = message
        this.errorStatus = errorCodeEnum.code
    }

    constructor(status: String, message: String, errorStatus: Int) : super(message) {
        this.status = status
        this.message = message
        this.errorStatus = errorStatus
    }
}
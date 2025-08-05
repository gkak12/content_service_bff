package com.service.common.exception

import com.service.common.enums.ErrorCodeEnums

class ContentException : RuntimeException {
    val value: String
    override val message: String
    val errorStatus: Int

    constructor(errorCodeEnums: ErrorCodeEnums) : super(errorCodeEnums.msg) {
        this.value = errorCodeEnums.value
        this.message = errorCodeEnums.msg
        this.errorStatus = errorCodeEnums.status
    }

    constructor(errorCodeEnums: ErrorCodeEnums, message: String) : super(message) {
        this.value = errorCodeEnums.value
        this.message = message
        this.errorStatus = errorCodeEnums.status
    }

    constructor(status: String, message: String, errorStatus: Int) : super(message) {
        this.value = status
        this.message = message
        this.errorStatus = errorStatus
    }
}
package com.service.common.enums

enum class ErrorCodeEnum (val value: String, val msg: String, val code: Int) {
    VALIDATION_CHECK("VALIDATION_CHECK", "검증에 실패하였습니다.", 400),
    UNAUTHENTICATED("UNAUTHENTICATED", "인증되지 않은 사용자입니다.", 401),
    PERMISSION_DENIED("PERMISSION_DENIED", "권한이 없는 사용자입니다.", 403),
    NOT_FOUND("NOT_FOUND", "요청하신 리소스가 존재하지 않습니다.", 404),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "지원하지 않는 HTTP 메소드입니다.", 405),
    ALREADY_EXIST("ALREADY_EXIST", "이미 존재하는 리소스입니다.", 409),
    RESOURCE_GONE("RESOURCE_GONE", "이미 삭제된 리소스입니다.", 410),
}
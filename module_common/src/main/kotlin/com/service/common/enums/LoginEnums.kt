package com.service.common.enums

enum class LoginEnums(val value: String, val msg: String) {
    NAVER_OAUTH2("naver", "네이버 OAUTH2 로그인"),
    NAVER_OAUTH2_EMAIL("email", "네이버 OAUTH2 계정 이메일"),
    NAVER_OAUTH2_NAME("name", "네이버 OAUTH2 계정 이름")
}
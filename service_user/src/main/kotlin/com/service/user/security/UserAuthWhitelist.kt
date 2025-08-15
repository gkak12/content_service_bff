package com.service.user.security

object UserAuthWhitelist {
    val paths = arrayOf(
        "/api/user/login",
        "/api/user/signup",
        "/api/user/oauth2/me",
        "/login/oauth2/**",
        "/api/user/jwt/refresh"
    )
}
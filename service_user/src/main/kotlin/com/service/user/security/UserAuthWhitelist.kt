package com.service.user.security

object UserAuthWhitelist {
    val paths = arrayOf(
        "/api/user/login",
        "/api/user/signup",
        "/api/user/oauth2/me/**",
        "/login/oauth2/**",
        "/oauth2/authorization/**",
        "/api/user/jwt/refresh"
    )
}
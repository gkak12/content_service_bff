package com.service.admin.security

object AdminAuthWhitelist {
    val paths = arrayOf("/api/admin/login", "/api/admin/signup", "/api/jwt/refresh")
}
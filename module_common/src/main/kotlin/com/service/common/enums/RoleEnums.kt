package com.service.common.enums

enum class RoleEnums(val value: String, val msg: String) {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN", "슈퍼 관리자 권한"),
    ROLE_ADMIN("ROLE_ADMIN", "일반 관리자 권한"),
    ROLE_USER("ROLE_USER", "일반 사용자 권한")
}
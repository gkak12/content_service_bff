package com.service.user.model.dto

data class UserDto(
    var userId: String = "",
    var userPassword: String = "",
    var userLoginType: String = "",
    var userName: String = "",
    var userEmail: String = "",
    var userAddress: String = ""
)
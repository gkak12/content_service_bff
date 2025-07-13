package com.service.admin.service

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestUserLoginDto
import com.service.admin.model.response.ResponseJwtTokenDto

interface AdminService {

    fun login(adminLoginDto: RequestUserLoginDto): ResponseJwtTokenDto
    fun findAdminByName(name: String): List<AdminDto>
}
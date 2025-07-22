package com.service.admin.service

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestAdminLoginDto
import com.service.admin.model.request.RequestAdminSignupDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.model.response.ResponseSignupDto

interface AdminService {

    fun login(adminLoginDto: RequestAdminLoginDto): ResponseJwtTokenDto
    fun signup(adminSignDto: RequestAdminSignupDto): ResponseSignupDto
    fun findAdminByName(name: String): List<AdminDto>
}
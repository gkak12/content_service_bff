package com.service.admin.service

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestAdminLoginDto
import com.service.admin.model.request.RequestAdminSignupDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.model.response.ResponseSignupDto
import reactor.core.publisher.Mono

interface AdminService {

    fun login(adminLoginDto: RequestAdminLoginDto): Mono<ResponseJwtTokenDto>
    fun signup(adminSignDto: RequestAdminSignupDto): Mono<ResponseSignupDto>
    fun findAdminByName(name: String): Mono<List<AdminDto>>
}
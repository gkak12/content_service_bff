package com.service.admin.service

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestAdminLoginDto
import com.service.admin.model.request.RequestAdminPasswordDto
import com.service.admin.model.request.RequestAdminSignupDto
import com.service.admin.model.request.RequestAdminUpdateDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.model.response.ResponseSignupDto
import com.service.admin.model.response.ResponseAdminDto
import reactor.core.publisher.Mono

interface AdminService {

    fun login(adminLoginDto: RequestAdminLoginDto): Mono<ResponseJwtTokenDto>
    fun signup(adminSignDto: RequestAdminSignupDto): Mono<ResponseSignupDto>
    fun findAdminByName(name: String): Mono<List<AdminDto>>
    fun update(adminUpdateDto: RequestAdminUpdateDto): Mono<ResponseAdminDto>
    fun delete(id: String): Mono<ResponseAdminDto>
    fun resetPassword(adminPasswordDto: RequestAdminPasswordDto): Mono<ResponseAdminDto>
}
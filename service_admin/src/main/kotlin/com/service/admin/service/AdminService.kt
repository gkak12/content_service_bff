package com.service.admin.service

import com.service.admin.domain.dto.response.AdminDtoResponse

interface AdminService {

    fun findAdminByName(name: String) : AdminDtoResponse
}
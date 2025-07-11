package com.service.admin.service

import com.service.admin.model.dto.AdminDto

interface AdminService {

    fun findAdminByName(name: String) : List<AdminDto>
}
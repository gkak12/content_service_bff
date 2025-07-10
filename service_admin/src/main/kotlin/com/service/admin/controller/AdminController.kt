package com.service.admin.controller

import com.service.admin.domain.dto.response.AdminDtoResponse
import com.service.admin.service.AdminService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/admin")
class AdminController (
    private val adminService: AdminService
){

    @GetMapping("/name")
    fun findAdminByName(@RequestParam(value = "name", defaultValue = "") name:String): Mono<AdminDtoResponse>?{
        return Mono.just(adminService.findAdminByName(name))
    }
}
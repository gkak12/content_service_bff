package com.service.admin.controller

import com.service.admin.model.dto.AdminDto
import com.service.admin.service.AdminService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/admin")
class AdminController (
    private val adminService: AdminService
){

    @GetMapping("/name")
    fun findAdminByName(@RequestParam(value = "name", defaultValue = "") name:String): Flux<AdminDto>{
        return Flux.fromIterable(adminService.findAdminByName(name))
    }
}
package com.service.admin.controller

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestUserLoginDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.service.AdminService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/admin")
class AdminController (
    private val adminService: AdminService
){

    @PostMapping("/login")
    fun login(@Valid @RequestBody adminLoginDto: RequestUserLoginDto): Mono<ResponseJwtTokenDto> {
        return Mono.just(adminService.login(adminLoginDto))
    }

    @GetMapping("/name")
    fun findAdminByName(@RequestParam(value = "name", defaultValue = "") name:String): Flux<AdminDto>{
        return Flux.fromIterable(adminService.findAdminByName(name))
    }
}
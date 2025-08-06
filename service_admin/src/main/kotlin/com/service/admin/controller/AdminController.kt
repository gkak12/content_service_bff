package com.service.admin.controller

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestAdminLoginDto
import com.service.admin.model.request.RequestAdminPasswordDto
import com.service.admin.model.request.RequestAdminSignupDto
import com.service.admin.model.request.RequestAdminUpdateDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.model.response.ResponseSignupDto
import com.service.admin.model.response.ResponseAdminDto
import com.service.admin.service.AdminService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
){

    @PostMapping("/login")
    fun login(@Valid @RequestBody adminLoginDto: RequestAdminLoginDto): Mono<ResponseJwtTokenDto> {
        return adminService.login(adminLoginDto)
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody adminDto: RequestAdminSignupDto): Mono<ResponseSignupDto> {
        return adminService.signup(adminDto)
    }

    @GetMapping("/name")
    fun findAdminByName(@RequestParam(value = "name", defaultValue = "") name:String): Mono<List<AdminDto>> {
        return adminService.findAdminByName(name)
    }

    @PatchMapping("/update")
    fun update(@Valid @RequestBody adminDto: RequestAdminUpdateDto): Mono<ResponseAdminDto> {
        return adminService.update(adminDto)
    }

    @DeleteMapping("/delete")
    fun delete(@RequestParam(value = "id") id: String): Mono<ResponseAdminDto> {
        return adminService.delete(id)
    }

    @PatchMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody adminPasswordDto: RequestAdminPasswordDto): Mono<ResponseAdminDto> {
        return adminService.resetPassword(adminPasswordDto)
    }
}

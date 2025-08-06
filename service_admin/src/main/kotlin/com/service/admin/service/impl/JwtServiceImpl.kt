package com.service.admin.service.impl

import com.service.admin.model.dto.AdminDto
import com.service.admin.model.request.RequestJwtTokenDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.security.JwtUtil
import com.service.admin.service.JwtService
import com.service.common.enums.JwtEnums
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class JwtServiceImpl(
    private val jwtUtil: JwtUtil
): JwtService {

    override fun refresh(jwtToken: RequestJwtTokenDto): Mono<ResponseJwtTokenDto> {
        return try {
            val refreshToken = jwtToken.refreshToken
            val id = jwtUtil.getAdminId(refreshToken)
            val role = jwtUtil.getAdminRole(refreshToken)

            if (!jwtUtil.validateToken(refreshToken, id, role)) {
                return Mono.error(IllegalArgumentException("Invalid refresh token"))
            }

            val adminDto = AdminDto(adminId = id, adminType = role)
            val newAccessToken = jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, adminDto)
            val newRefreshToken = jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, adminDto)

            val response = ResponseJwtTokenDto(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )

            Mono.just(response)
        } catch (e: Exception) {
            Mono.error(RuntimeException("Failed to refresh token", e))
        }
    }
}
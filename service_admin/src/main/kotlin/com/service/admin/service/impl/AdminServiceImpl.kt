package com.service.admin.service.impl

import com.service.account.GrpcAdminProtoDto
import com.service.account.GrpcAdminRequest
import com.service.admin.model.dto.AdminDto
import com.service.admin.model.mapper.AdminMapper
import com.service.admin.model.request.RequestAdminLoginDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.security.JwtUtil
import com.service.admin.service.AdminService
import com.service.common.enums.ErrorCodeEnum
import com.service.common.enums.JwtEnums
import com.service.common.exception.ContentException
import com.service.common.util.RedisUtil
import com.service.grpc.service.GrpcClientAdminService
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val grpcClientAdminService: GrpcClientAdminService,
    private val adminMapper: AdminMapper,
    private val redisUtil: RedisUtil,
    private val jwtUtil: JwtUtil
) : AdminService {

    override fun login(adminLoginDto: RequestAdminLoginDto): ResponseJwtTokenDto {
        val response = grpcClientAdminService.login(
                GrpcAdminProtoDto.newBuilder()
                    .setAdminId(adminLoginDto.id)
                    .setAdminPassword(adminLoginDto.password)
                    .build()
            )

        if(response.statusCode != "OK"){
            throw ContentException(ErrorCodeEnum.INTERNAL_SERVER_ERROR)
        }

        val userId = adminLoginDto.id
        val accessToken = jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, userId)
        val refreshToken = jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, userId)

        redisUtil.setRefreshToken(userId+JwtEnums.TOKEN_KEY.value, refreshToken)

        return ResponseJwtTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    override fun findAdminByName(name: String): List<AdminDto> {
        val list = grpcClientAdminService.findAdminByName(
                GrpcAdminRequest.newBuilder()
                    .setAdminName(name)
                    .build()
            ).dtoList.stream()
            .map { adminMapper.toDto(it) }
            .toList()

        return list
    }
}
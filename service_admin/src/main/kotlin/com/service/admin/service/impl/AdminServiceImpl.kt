package com.service.admin.service.impl

import com.service.account.GrpcAdminProtoDto
import com.service.account.GrpcAdminRequest
import com.service.admin.model.dto.AdminDto
import com.service.admin.model.mapper.AdminMapper
import com.service.admin.model.request.RequestAdminLoginDto
import com.service.admin.model.request.RequestAdminSignupDto
import com.service.admin.model.response.ResponseJwtTokenDto
import com.service.admin.model.response.ResponseSignupDto
import com.service.admin.security.JwtUtil
import com.service.admin.service.AdminService
import com.service.common.enums.ErrorCodeEnum
import com.service.common.enums.JwtEnums
import com.service.common.exception.ContentException
import com.service.common.util.RedisUtil
import com.service.grpc.service.GrpcClientAdminService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AdminServiceImpl(
    private val grpcClientAdminService: GrpcClientAdminService,
    private val adminMapper: AdminMapper,
    private val redisUtil: RedisUtil,
    private val jwtUtil: JwtUtil
) : AdminService {

    override fun login(adminLoginDto: RequestAdminLoginDto): Mono<ResponseJwtTokenDto> {
        return grpcClientAdminService.login(
            GrpcAdminProtoDto.newBuilder()
                .setAdminId(adminLoginDto.id)
                .setAdminPassword(adminLoginDto.password)
                .build()
        ).map { response ->
            if (response.statusCode != "OK") {
                throw ContentException(ErrorCodeEnum.INTERNAL_SERVER_ERROR)
            }

            val protoDto = response.dtoList[0]
            val accessToken = jwtUtil.createToken(JwtEnums.ACCESS_TYPE.value, protoDto)
            val refreshToken = jwtUtil.createToken(JwtEnums.REFRESH_TYPE.value, protoDto)

            redisUtil.setRefreshToken(protoDto.adminId+JwtEnums.TOKEN_KEY.value, refreshToken)

            ResponseJwtTokenDto(accessToken, refreshToken)
        }
    }

    override fun signup(adminSignDto: RequestAdminSignupDto): Mono<ResponseSignupDto> {
        return grpcClientAdminService.signup(
            GrpcAdminProtoDto.newBuilder()
                .setAdminId(adminSignDto.id)
                .setAdminName(adminSignDto.name)
                .setAdminPassword(adminSignDto.password)
                .setEmail(adminSignDto.email)
                .build()
        ).map { response ->
            if (response.statusCode != "OK") {
                throw ContentException(ErrorCodeEnum.INTERNAL_SERVER_ERROR)
            }

            ResponseSignupDto(message = "${adminSignDto.id} is signed.")
        }
    }

    override fun findAdminByName(name: String): Mono<List<AdminDto>> {
        return grpcClientAdminService.findAdminByName(
            GrpcAdminRequest.newBuilder()
                .setAdminName(name)
                .build()
        ).map { response ->
            response.dtoList.map { adminMapper.toDto(it) }
        }
    }
}

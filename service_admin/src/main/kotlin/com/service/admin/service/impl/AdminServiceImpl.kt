package com.service.admin.service.impl

import com.service.account.GrpcAdminRequest
import com.service.admin.model.dto.AdminDto
import com.service.admin.model.mapper.AdminMapper
import com.service.admin.service.AdminService
import com.service.grpc.service.GrpcClientAdminService
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val grpcClientAdminService: GrpcClientAdminService,
    private val adminMapper: AdminMapper
) : AdminService {

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
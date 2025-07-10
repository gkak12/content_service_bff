package com.service.admin.service.impl

import com.service.account.GrpcAdminRequest
import com.service.admin.domain.dto.response.AdminDtoResponse
import com.service.admin.service.AdminService
import com.service.grpc.service.GrpcClientAdminService
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val grpcClientAdminService: GrpcClientAdminService
) : AdminService {

    override fun findAdminByName(name: String): AdminDtoResponse {
        val grpcAdminResponse = grpcClientAdminService.findAdminByName(
            GrpcAdminRequest.newBuilder()
                .setAdminName(name)
                .build()
        )

        return AdminDtoResponse(
            adminId = grpcAdminResponse.adminId,
            adminName = grpcAdminResponse.adminName,
            email = grpcAdminResponse.email
        )
    }
}
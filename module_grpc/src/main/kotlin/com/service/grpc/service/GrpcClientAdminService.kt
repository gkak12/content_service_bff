package com.service.grpc.service

import com.service.account.GrpcAdminRequest
import com.service.account.GrpcAdminResponse
import com.service.account.GrpcAdminServiceGrpc
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class GrpcClientAdminService {

    @GrpcClient("admin-service")
    private lateinit var adminServiceChannel: Channel

    private fun adminStub(): GrpcAdminServiceGrpc.GrpcAdminServiceBlockingStub {
        return GrpcAdminServiceGrpc.newBlockingStub(adminServiceChannel)
    }

    fun findAdminByName(request: GrpcAdminRequest): GrpcAdminResponse {
        return adminStub().findAdminByName(request)
    }
}
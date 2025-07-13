package com.service.grpc.service

import com.service.account.GrpcAdminProtoDto
import com.service.account.GrpcAdminRequest
import com.service.account.GrpcAdminResponse
import com.service.account.GrpcAdminServiceGrpc
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class GrpcClientAdminService {

    @GrpcClient("service-account")
    private lateinit var serviceAccountChannel: Channel

    private fun adminStub(): GrpcAdminServiceGrpc.GrpcAdminServiceBlockingStub {
        return GrpcAdminServiceGrpc.newBlockingStub(serviceAccountChannel)
    }

    fun login(protoDto: GrpcAdminProtoDto): GrpcAdminResponse {
        return adminStub().login(protoDto)
    }

    fun findAdminByName(request: GrpcAdminRequest): GrpcAdminResponse {
        return adminStub().findAdminByName(request)
    }
}

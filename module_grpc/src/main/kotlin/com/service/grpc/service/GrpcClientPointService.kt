package com.service.grpc.service

import com.service.account.GrpcAdminResponse
import com.service.point.GrpcPointRequest
import com.service.point.GrpcPointResponse
import com.service.point.GrpcPointServiceGrpc
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GrpcClientPointService {

    @GrpcClient("service-point")
    private lateinit var servicePointChannel: Channel

    // 논블로킹 stub 생성
    private fun pointAsyncStub(): GrpcPointServiceGrpc.GrpcPointServiceStub {
        return GrpcPointServiceGrpc.newStub(servicePointChannel)
    }

    // 포인트 페이징 (논블로킹)
    fun findPointById(request: GrpcPointRequest): Mono<GrpcPointResponse> {
        return Mono.create { sink ->
            pointAsyncStub().findPointById(request, object : io.grpc.stub.StreamObserver<GrpcPointResponse> {
                override fun onNext(value: GrpcPointResponse) {
                    sink.success(value)  // 응답을 받으면 성공 처리
                }

                override fun onError(t: Throwable) {
                    sink.error(t)  // 오류가 발생하면 에러 처리
                }

                override fun onCompleted() {
                    // 완료시 후처리
                }
            })
        }
    }

}
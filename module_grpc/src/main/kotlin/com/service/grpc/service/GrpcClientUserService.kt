package com.service.grpc.service

import com.service.account.*
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GrpcClientUserService {

    @GrpcClient("service-account")
    private lateinit var serviceAccountChannel: Channel

    // 논블로킹 stub 생성
    private fun userAsyncStub(): GrpcUserServiceGrpc.GrpcUserServiceStub {
        return GrpcUserServiceGrpc.newStub(serviceAccountChannel)
    }

    // 사용자 아이디로 검색 (논블로킹)
    fun findUserById(request: GrpcUserRequest): Mono<GrpcUserResponse> {
        return Mono.create{ sink ->
            userAsyncStub().findUserById(request, object: io.grpc.stub.StreamObserver<GrpcUserResponse>{
                override fun onNext(value: GrpcUserResponse) {
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

    // 사용자 이름으로 검색 (논블로킹)
    fun findUserByName(request: GrpcUserRequest): Mono<GrpcUserResponse> {
        return Mono.create{ sink ->
            userAsyncStub().findUserByName(request, object: io.grpc.stub.StreamObserver<GrpcUserResponse>{
                override fun onNext(value: GrpcUserResponse) {
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

    // 사용자 등록 (논블로킹)
    fun loginUser(protoDto: GrpcUserProtoDto): Mono<GrpcUserResponse> {
        return Mono.create{ sink ->
            userAsyncStub().loginUser(protoDto, object: io.grpc.stub.StreamObserver<GrpcUserResponse>{
                override fun onNext(value: GrpcUserResponse) {
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

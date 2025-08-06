package com.service.grpc.service

import com.service.account.GrpcAdminProtoDto
import com.service.account.GrpcAdminRequest
import com.service.account.GrpcAdminResponse
import com.service.account.GrpcAdminServiceGrpc
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GrpcClientAdminService {

    @GrpcClient("service-account")
    private lateinit var serviceAccountChannel: Channel

    // 논블로킹 stub 생성
    private fun adminAsyncStub(): GrpcAdminServiceGrpc.GrpcAdminServiceStub {
        return GrpcAdminServiceGrpc.newStub(serviceAccountChannel)
    }

    // 로그인 요청 (논블로킹)
    fun login(protoDto: GrpcAdminProtoDto): Mono<GrpcAdminResponse> {
        return Mono.create { sink ->
            adminAsyncStub().login(protoDto, object : io.grpc.stub.StreamObserver<GrpcAdminResponse> {
                override fun onNext(value: GrpcAdminResponse) {
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

    // 회원가입 요청 (논블로킹)
    fun signup(protoDto: GrpcAdminProtoDto): Mono<GrpcAdminResponse> {
        return Mono.create { sink ->
            adminAsyncStub().signup(protoDto, object : io.grpc.stub.StreamObserver<GrpcAdminResponse> {
                override fun onNext(value: GrpcAdminResponse) {
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

    // 이름으로 관리자를 조회 (논블로킹)
    fun findAdminByName(request: GrpcAdminRequest): Mono<GrpcAdminResponse> {
        return Mono.create { sink ->
            adminAsyncStub().findAdminByName(request, object : io.grpc.stub.StreamObserver<GrpcAdminResponse> {
                override fun onNext(value: GrpcAdminResponse) {
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

    // 관리자 수정 (논블로킹)
    fun update(protoDto: GrpcAdminProtoDto): Mono<GrpcAdminResponse> {
        return Mono.create { sink ->
            adminAsyncStub().update(protoDto, object : io.grpc.stub.StreamObserver<GrpcAdminResponse> {
                override fun onNext(value: GrpcAdminResponse) {
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

    // 관리자 삭제 (논블로킹)
    fun delete(protoDto: GrpcAdminProtoDto): Mono<GrpcAdminResponse> {
        return Mono.create { sink ->
            adminAsyncStub().delete(protoDto, object : io.grpc.stub.StreamObserver<GrpcAdminResponse> {
                override fun onNext(value: GrpcAdminResponse) {
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

    // 관리자 비밀번호 수정 (논블로킹)
    fun resetPassword(protoDto: GrpcAdminProtoDto): Mono<GrpcAdminResponse> {
        return Mono.create { sink ->
            adminAsyncStub().resetPassword(protoDto, object : io.grpc.stub.StreamObserver<GrpcAdminResponse> {
                override fun onNext(value: GrpcAdminResponse) {
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

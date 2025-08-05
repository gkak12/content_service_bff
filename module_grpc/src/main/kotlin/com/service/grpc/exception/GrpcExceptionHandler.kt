package com.service.grpc.exception

import com.service.common.enums.ErrorCodeEnums
import com.service.common.exception.ContentExceptionHandler
import com.service.common.response.ErrorResponse
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GrpcExceptionHandler: ContentExceptionHandler() {

    private val log = LoggerFactory.getLogger(GrpcExceptionHandler::class.java)

    /**
     * gRPC 예외 처리
     */
    @ExceptionHandler(StatusRuntimeException::class)
    fun handleStatusRuntimeException(e: StatusRuntimeException): ResponseEntity<ErrorResponse> {
        log.error(e.message, e)

        val errorCode = when (e.status.code) {
            io.grpc.Status.Code.INVALID_ARGUMENT -> ErrorCodeEnums.VALIDATION_CHECK
            io.grpc.Status.Code.UNAUTHENTICATED -> ErrorCodeEnums.UNAUTHENTICATED
            io.grpc.Status.Code.PERMISSION_DENIED -> ErrorCodeEnums.PERMISSION_DENIED
            io.grpc.Status.Code.NOT_FOUND -> ErrorCodeEnums.NOT_FOUND
            io.grpc.Status.Code.ALREADY_EXISTS -> ErrorCodeEnums.ALREADY_EXIST
            io.grpc.Status.Code.FAILED_PRECONDITION -> ErrorCodeEnums.RESOURCE_GONE
            else -> ErrorCodeEnums.INTERNAL_SERVER_ERROR
        }

        val response = ErrorResponse(
            errorCode,
            e.status.description ?: "gRPC 호출 중 오류가 발생했습니다."
        )

        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }
}

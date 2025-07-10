package com.service.grpc.exception

//import com.service.common.enums.ErrorCodeEnum
//import com.service.common.exception.ContentExceptionHandler
//import com.service.common.response.ErrorResponse
//import io.grpc.StatusRuntimeException
//import org.slf4j.LoggerFactory
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.RestControllerAdvice

//@RestControllerAdvice
class GrpcExceptionHandler
//    : ContentExceptionHandler() {
//
//    private val log = LoggerFactory.getLogger(GrpcExceptionHandler::class.java)
//
//    /**
//     * gRPC 예외 처리
//     */
//    @ExceptionHandler(StatusRuntimeException::class)
//    fun handleStatusRuntimeException(e: StatusRuntimeException): ResponseEntity<ErrorResponse> {
//        log.error(e.message, e)
//
//        val errorCode = when (e.status.code) {
//            io.grpc.Status.Code.INVALID_ARGUMENT -> ErrorCodeEnum.VALIDATION_CHECK
//            io.grpc.Status.Code.UNAUTHENTICATED -> ErrorCodeEnum.UNAUTHENTICATED
//            io.grpc.Status.Code.PERMISSION_DENIED -> ErrorCodeEnum.PERMISSION_DENIED
//            io.grpc.Status.Code.NOT_FOUND -> ErrorCodeEnum.NOT_FOUND
//            io.grpc.Status.Code.ALREADY_EXISTS -> ErrorCodeEnum.ALREADY_EXIST
//            io.grpc.Status.Code.FAILED_PRECONDITION -> ErrorCodeEnum.RESOURCE_GONE
//            else -> ErrorCodeEnum.INTERNAL_SERVER_ERROR
//        }
//
//        val response = ErrorResponse(
//            errorCode,
//            e.status.description ?: "gRPC 호출 중 오류가 발생했습니다."
//        )
//
//        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
//    }
//}

package com.service.common.exception

import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import com.service.common.enums.ErrorCodeEnum
import com.service.common.response.ErrorResponse
import com.service.common.response.ValidationErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.MethodNotAllowedException

@RestControllerAdvice
class ContentExceptionHandler {

    private val log = LoggerFactory.getLogger(ContentExceptionHandler::class.java)

    private fun getRootCause(throwable: Throwable): Throwable {
        var cause = throwable
        while (cause.cause != null && cause.cause != cause) {
            cause = cause.cause!!
        }
        return cause
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val rootCause = getRootCause(ex)

        val response = if (rootCause is ContentException) {
            ErrorResponse(ErrorCodeEnum.VALIDATION_CHECK, rootCause.message ?: "")
        } else {
            ErrorResponse(ErrorCodeEnum.VALIDATION_CHECK, "잘못된 형식의 JSON 데이터입니다.")
        }

        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(ErrorCodeEnum.VALIDATION_CHECK.status)))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class, BindException::class)
    fun handleValidationException(ex: Exception): Mono<ResponseEntity<ValidationErrorResponse>> {
        log.error(ex.message, ex)

        val bindingResult: BindingResult? = when (ex) {
            is MethodArgumentNotValidException -> ex.bindingResult
            is BindException -> ex.bindingResult
            else -> null
        }

        val response = ValidationErrorResponse(bindingResult)
        return Mono.just(ResponseEntity(response, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ErrorCodeEnum.VALIDATION_CHECK, "잘못된 enum 값입니다.")
        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(ErrorCodeEnum.VALIDATION_CHECK.status)))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ErrorCodeEnum.VALIDATION_CHECK, ex.message ?: "")
        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(ErrorCodeEnum.VALIDATION_CHECK.status)))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ErrorCodeEnum.UNAUTHENTICATED, ErrorCodeEnum.UNAUTHENTICATED.msg)
        return Mono.just(ResponseEntity(response, HttpStatus.UNAUTHORIZED))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ErrorCodeEnum.PERMISSION_DENIED, ErrorCodeEnum.PERMISSION_DENIED.msg)
        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(ErrorCodeEnum.PERMISSION_DENIED.status)))
    }

    @ExceptionHandler(MethodNotAllowedException::class)
    fun handleMethodNotAllowedException(ex: MethodNotAllowedException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ErrorCodeEnum.METHOD_NOT_ALLOWED, ErrorCodeEnum.METHOD_NOT_ALLOWED.msg)
        return Mono.just(ResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED))
    }

    @ExceptionHandler(ContentException::class)
    fun handleContentException(ex: ContentException): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ex.value, ex.message ?: "")
        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(ex.errorStatus)))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): Mono<ResponseEntity<ErrorResponse>> {
        log.error(ex.message, ex)
        val response = ErrorResponse(ErrorCodeEnum.INTERNAL_SERVER_ERROR, ErrorCodeEnum.INTERNAL_SERVER_ERROR.msg)
        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(ErrorCodeEnum.INTERNAL_SERVER_ERROR.status)))
    }
}

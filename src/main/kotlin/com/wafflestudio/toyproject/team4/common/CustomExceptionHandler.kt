package com.wafflestudio.toyproject.team4.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity(e.javaClass, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [CustomHttpException::class])
    fun handle(e: CustomHttpException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = HashMap<String, String?>()
        e.bindingResult.allErrors.forEach { error ->
            errors[(error as FieldError).field] = error.defaultMessage
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<Any> {
        return ResponseEntity("잘못된 요청입니다.", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [MissingRequestHeaderException::class])
    fun handle(e: MissingRequestHeaderException): ResponseEntity<Any> {
        return ResponseEntity("사용자 식별 불가: 접근할 수 없습니다.", HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [MissingRequestCookieException::class])
    fun handle(e: MissingRequestCookieException): ResponseEntity<Any> {
        return ResponseEntity("쿠키에 토큰 갱신을 위한 정보가 없습니다.", HttpStatus.BAD_REQUEST)
    }
}
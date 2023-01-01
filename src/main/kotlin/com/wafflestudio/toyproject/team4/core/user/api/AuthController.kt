package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.core.user.api.request.LoginRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @Transactional
    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest
    ) = ResponseEntity(authService.register(registerRequest), HttpStatus.CREATED)

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ) = authService.login(loginRequest)
}
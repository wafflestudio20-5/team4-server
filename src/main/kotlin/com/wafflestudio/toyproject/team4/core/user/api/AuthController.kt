package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.user.api.request.LoginRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.UsernameRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.UsernameResponse
import com.wafflestudio.toyproject.team4.core.user.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(value = "refreshToken") refreshToken: String,
    ) = authService.refresh(refreshToken)

    @Authenticated
    @PostMapping("/logout")
    fun logout(
        @UserContext username: String
    ) = authService.logout(username)

    @PostMapping("/username")
    fun checkDuplicatedUsername(
        @RequestBody usernameRequest: UsernameRequest
    ): UsernameResponse {
        return authService.checkDuplicatedUsername(usernameRequest)
    }
}

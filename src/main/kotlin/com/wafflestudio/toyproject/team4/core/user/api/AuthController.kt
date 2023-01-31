package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.user.api.request.LoginRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.NicknameRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PasswordRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.UsernameRequest
import com.wafflestudio.toyproject.team4.core.user.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
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

    @Authenticated
    @PostMapping("/social-login")
    fun socialLogin(
        @UserContext username: String
    ) = authService.socialLogin(username)

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
    ) = authService.checkDuplicatedUsername(usernameRequest)

    @PostMapping("/nickname")
    fun checkDuplicatedNickname(
        @RequestBody nicknameRequest: NicknameRequest
    ) = authService.checkDuplicatedNickname(nicknameRequest)

    @Authenticated
    @PostMapping("/password")
    fun checkCurrentPassword(
        @RequestBody passwordRequest: PasswordRequest,
        @UserContext username: String
    ) = authService.checkCurrentPassword(username, passwordRequest)
}

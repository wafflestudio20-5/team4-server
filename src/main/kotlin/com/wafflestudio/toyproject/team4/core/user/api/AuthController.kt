package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional


@RestController
class AuthController (
    private val authService: AuthService
){
    
    @Transactional
    @PostMapping("api/auth/register")
    fun register(
        @RequestBody request: RegisterRequest
    ) = ResponseEntity(authService.register(request), HttpStatus.CREATED)
}
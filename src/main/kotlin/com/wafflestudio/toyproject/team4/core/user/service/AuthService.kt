package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.Seminar401
import com.wafflestudio.toyproject.team4.common.Seminar404
import com.wafflestudio.toyproject.team4.core.user.api.request.LoginRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.LoginResponse
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.domain.User
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface AuthService {
    fun register(registerRequest: RegisterRequest)

    fun login(loginRequest: LoginRequest): ResponseEntity<LoginResponse>
}

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
) : AuthService {
    @Transactional
    override fun register(registerRequest: RegisterRequest) {
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        userRepository.save(registerRequest.toUserEntity(encodedPassword))
    }

    @Transactional
    override fun login(loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw Seminar404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        if (!passwordEncoder.matches(loginRequest.password, user.encodedPassword)) {
            throw Seminar401("비밀번호가 올바르지 않습니다.")
        }

        val accessToken = authTokenService.generateTokenByUsername(loginRequest.username).accessToken
        val refreshToken = authTokenService.generateRefreshTokenByUsername(loginRequest.username)
        user.refreshToken = refreshToken
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, authTokenService.generateCookie(refreshToken).toString())
            .body(LoginResponse(User.of(user), accessToken))
    }
}
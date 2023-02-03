package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp401
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.image.service.ImageService
import com.wafflestudio.toyproject.team4.core.user.api.request.LoginRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.NicknameRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PasswordRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.UsernameRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.AuthToken
import com.wafflestudio.toyproject.team4.core.user.api.response.NicknameResponse
import com.wafflestudio.toyproject.team4.core.user.api.response.UsernameResponse
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface AuthService {
    fun register(registerRequest: RegisterRequest)

    fun login(loginRequest: LoginRequest): ResponseEntity<AuthToken>

    fun socialLogin(username: String): ResponseEntity<AuthToken>

    fun refresh(refreshToken: String): ResponseEntity<AuthToken>

    fun logout(username: String)

    fun checkDuplicatedUsername(usernameRequest: UsernameRequest): UsernameResponse

    fun checkDuplicatedNickname(nicknameRequest: NicknameRequest): NicknameResponse

    fun checkCurrentPassword(username: String, passwordRequest: PasswordRequest): ResponseEntity<Unit>
}

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val imageService: ImageService
) : AuthService {
    @Transactional
    override fun register(registerRequest: RegisterRequest) {
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        val user = registerRequest.toUserEntity(encodedPassword)
        user.image = imageService.getDefaultImage(registerRequest.username)
        userRepository.save(user)
    }

    @Transactional
    override fun login(loginRequest: LoginRequest): ResponseEntity<AuthToken> {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        if (!passwordEncoder.matches(loginRequest.password, user.encodedPassword)) {
            throw CustomHttp401("비밀번호가 올바르지 않습니다.")
        }

        val accessToken = authTokenService.generateAccessTokenByUsername(loginRequest.username).accessToken
        val refreshToken = authTokenService.generateRefreshTokenByUsername(loginRequest.username)
        user.refreshToken = refreshToken
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, authTokenService.generateResponseCookie(refreshToken).toString())
            .body(AuthToken(accessToken))
    }

    @Transactional
    override fun socialLogin(username: String): ResponseEntity<AuthToken> {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val accessToken = authTokenService.generateAccessTokenByUsername(username).accessToken
        val refreshToken = authTokenService.generateRefreshTokenByUsername(username)
        user.refreshToken = refreshToken
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, authTokenService.generateResponseCookie(refreshToken).toString())
            .body(AuthToken(accessToken))
    }

    @Transactional
    override fun refresh(refreshToken: String): ResponseEntity<AuthToken> {
        val username = authTokenService.getUsernameFromToken(refreshToken)
        val user = userRepository.findByUsername(username) ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        user.refreshToken ?: throw CustomHttp400("해당 사용자는 로그인 상태가 아닙니다.")
        if (refreshToken != user.refreshToken) throw CustomHttp400("잘못된 요청입니다.")

        val accessToken = authTokenService.generateAccessTokenByUsername(username).accessToken
        val newRefreshToken = authTokenService.generateRefreshTokenByUsername(username)
        user.refreshToken = newRefreshToken
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, authTokenService.generateResponseCookie(newRefreshToken).toString())
            .body(AuthToken(accessToken))
    }

    @Transactional
    override fun logout(username: String) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        user.refreshToken = null
    }

    override fun checkDuplicatedUsername(usernameRequest: UsernameRequest): UsernameResponse {
        val isUnique = userRepository.findByUsername(usernameRequest.username) === null
        return UsernameResponse(isUnique)
    }

    override fun checkDuplicatedNickname(nicknameRequest: NicknameRequest): NicknameResponse {
        val isUnique = userRepository.findByNickname(nicknameRequest.nickname) === null
        return NicknameResponse(isUnique)
    }

    override fun checkCurrentPassword(username: String, passwordRequest: PasswordRequest): ResponseEntity<Unit> {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        if (!passwordEncoder.matches(passwordRequest.currentPassword, user.encodedPassword)) {
            throw CustomHttp401("현재 비밀번호가 올바르지 않습니다.")
        }
        return ResponseEntity(HttpStatus.OK)
    }
}

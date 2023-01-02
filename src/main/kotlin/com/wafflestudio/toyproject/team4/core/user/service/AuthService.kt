package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.config.AuthConfig
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface AuthService {
    fun register(registerRequest: RegisterRequest)
}

@Service
class AuthServiceImpl(
    private val authConfig: AuthConfig,
    private val userRepository: UserRepository
): AuthService {
    
    @Transactional
    override fun register(registerRequest: RegisterRequest) {
        val encodedPwd = authConfig.passwordEncoder().encode(registerRequest.password)
        userRepository.save(registerRequest.toUserEntity(encodedPwd))
    }
}
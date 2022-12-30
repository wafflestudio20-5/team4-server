package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.config.AuthConfig
import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface AuthService {
    fun register(request: RegisterRequest)
}

@Service
class AuthServiceImpl(
    private val authConfig: AuthConfig,
    private val userRepository: UserRepository
): AuthService {
    
    @Transactional
    override fun register(request: RegisterRequest) {
        val encodedPwd = authConfig.passwordEncoder().encode(request.password)
        userRepository.save(request.toUserEntity(encodedPwd))
    }
}
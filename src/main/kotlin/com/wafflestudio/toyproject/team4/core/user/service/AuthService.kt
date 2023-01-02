package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface AuthService {
    fun register(registerRequest: RegisterRequest)
}

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
): AuthService {
    
    @Transactional
    override fun register(registerRequest: RegisterRequest) {
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        userRepository.save(registerRequest.toUserEntity(encodedPassword))
    }
}
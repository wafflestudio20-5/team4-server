package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.core.user.api.request.RegisterRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface AuthService {
    fun register(request: RegisterRequest)
}

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository
): AuthService {
    
    @Transactional
    override fun register(request: RegisterRequest) {
        userRepository.save(request.toUserEntity(request.password))
    }
}
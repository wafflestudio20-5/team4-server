package com.wafflestudio.toyproject.team4.core.user.api.request

import com.wafflestudio.toyproject.team4.core.user.domain.UserEntity

data class RegisterRequest(
    val username: String,
    val password: String,
    val nickname: String
) {
    fun toUserEntity(encodedPwd: String): UserEntity
    = UserEntity(username, encodedPwd, nickname)
}

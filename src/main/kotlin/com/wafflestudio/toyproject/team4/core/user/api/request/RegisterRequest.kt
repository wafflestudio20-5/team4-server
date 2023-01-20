package com.wafflestudio.toyproject.team4.core.user.api.request

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity

data class RegisterRequest(
    val username: String,
    val password: String,
    val nickname: String
) {
    fun toUserEntity(encodedPassword: String): UserEntity = UserEntity(
        username, encodedPassword, nickname
    )
}

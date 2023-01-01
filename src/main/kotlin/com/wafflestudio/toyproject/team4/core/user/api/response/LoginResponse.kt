package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.User

data class LoginResponse(
    val user: User,
    val accessToken: String
)
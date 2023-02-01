package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.User

data class UserSearchResponse(
    val users: List<User.Simplified>
)

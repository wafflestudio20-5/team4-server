package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.User

data class UserResponse(
    val user: User,
    val count: Count,
    val isFollow: Boolean,
)

data class Count(
    val styleCount: Long,
    val followerCount: Long,
    val followingCount: Long,
)

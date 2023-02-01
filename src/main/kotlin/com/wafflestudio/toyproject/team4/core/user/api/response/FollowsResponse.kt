package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.User

data class FollowersResponse(
    val followers: List<User.Simplified>
)

data class FollowingsResponse(
    val followings: List<User.Simplified>
)

package com.wafflestudio.toyproject.team4.core.user.api.response

data class FollowersResponse(
    val followers: List<UserFollow>
)

data class FollowingsResponse(
    val followings: List<UserFollow>
)

data class UserFollow(
    val id: Long,
    val username: String,
    val nickname: String,
    val image: String,
)

package com.wafflestudio.toyproject.team4.core.user.api.request

data class PatchMeRequest(
    val image: String?,
    val password: String?,
    val nickname: String?,
    val sex: String?,
    val height: Long?,
    val weight: Long?,
    val description: String?,
    val instaUsername: String?
)

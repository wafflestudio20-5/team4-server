package com.wafflestudio.toyproject.team4.core.user.api.request

data class PatchMeRequest(
    val image: String? = null,
    val password: String? = null,
    val nickname: String? = null,
    val sex: String? = null,
    val height: Long? = null,
    val weight: Long? = null,
    val description: String? = null,
    val instaUsername: String? = null
)

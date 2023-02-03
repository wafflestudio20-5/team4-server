package com.wafflestudio.toyproject.team4.core.style.api.request

data class PatchStyleRequest(
    val images: List<String>?,
    val itemIds: List<Long>?,
    val content: String?,
    val hashtag: String?
)

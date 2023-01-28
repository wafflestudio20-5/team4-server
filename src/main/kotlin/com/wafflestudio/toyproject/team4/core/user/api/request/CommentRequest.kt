package com.wafflestudio.toyproject.team4.core.user.api.request

data class CommentRequest(
    val reviewId: Long,
    val content: String,
)

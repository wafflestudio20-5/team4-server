package com.wafflestudio.toyproject.team4.core.board.api.request

data class PostCommentRequest(
    val reviewId: Long,
    val content: String,
)

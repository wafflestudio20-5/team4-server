package com.wafflestudio.toyproject.team4.core.user.api.request

data class ReviewRequest(
    val purchaseId: Long,
    val rating: Long,
    val content: String,
    val size: String,
    val color: String,
    val images: List<String>,
)
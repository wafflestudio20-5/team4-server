package com.wafflestudio.toyproject.team4.core.user.api.request

data class ReviewRequest(
    val id: Long,       //Post에서는 purchaseId, Put에서는 reviewId
    val rating: Long,
    val content: String,
    val size: String,
    val color: String,
    val images: List<String>,
)
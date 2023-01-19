package com.wafflestudio.toyproject.team4.core.board.api.response

import com.wafflestudio.toyproject.team4.core.board.domain.Review

data class ReviewsResponse(
    val reviews: List<Review>
)

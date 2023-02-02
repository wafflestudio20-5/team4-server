package com.wafflestudio.toyproject.team4.core.board.api.request

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity.Color
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity.Size

data class ReviewRequest(
    val id: Long, //Post에서는 purchaseId, Put에서는 reviewId
    val rating: Long,
    val content: String,
    val size: String,
    val color: String,
    val images: List<String>,
) {
    init {
        if (rating < 0 || rating > 10)
            throw CustomHttp400("구매만족도의 범위가 올바르지 않습니다.")

        try {
            Size.valueOf(size.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("사이즈가 적절하지 않습니다.")
        }

        try {
            Color.valueOf(color.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("색감이 적절하지 않습니다.")
        }
    }
}

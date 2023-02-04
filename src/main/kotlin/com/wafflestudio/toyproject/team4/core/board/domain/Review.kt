package com.wafflestudio.toyproject.team4.core.board.domain

import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity
import com.wafflestudio.toyproject.team4.core.purchase.domain.Purchase
import java.time.LocalDateTime

data class Review(
    val id: Long,
    val purchase: Purchase,
    val rating: Long,
    val content: String,
    val createdDateTime: LocalDateTime,
    val size: String,
    val color: String,
    val comments: List<Comment>,
    val images: List<String>,
) {
    companion object {
        fun of(reviewEntity: ReviewEntity) = reviewEntity.run {
            Review(
                id = id,
                purchase = Purchase.of(purchase),
                rating = rating,
                content = content,
                createdDateTime = createdDateTime,
                size = size.toString().lowercase(),
                color = color.toString().lowercase(),
                comments = comments
                    .sortedByDescending { it.createdDateTime }
                    .map { commentEntity -> Comment.of(commentEntity) },
                images = listOfNotNull(image1, image2, image3),
            )
        }
    }
}

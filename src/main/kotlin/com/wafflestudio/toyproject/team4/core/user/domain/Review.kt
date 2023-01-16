package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.user.database.ReviewEntity
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
                comments = comments.map { commentEntity -> Comment.of(commentEntity) },
                images = images.map { it.cloudinaryUrl },
            )
        }
    }
}
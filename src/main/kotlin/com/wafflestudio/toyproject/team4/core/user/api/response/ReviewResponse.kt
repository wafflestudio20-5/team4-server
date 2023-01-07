package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.database.Color
import com.wafflestudio.toyproject.team4.core.user.database.ReviewEntity
import com.wafflestudio.toyproject.team4.core.user.database.Size
import com.wafflestudio.toyproject.team4.core.user.domain.Comment
import com.wafflestudio.toyproject.team4.core.user.domain.User

data class ReviewResponse(
    val nickname: String,
    val sex: User.Sex?,
    val height: Long?,
    val weight: Long?,
    
    val itemName: String,
    val optionName: String?,
    val imageUrl: String,
    
    val rating: Long,
    val text: String,
    val size: Size,
    val color: Color,
) {
    var comments: MutableList<Comment>? = null    
    
    companion object {
        fun of(reviewEntity: ReviewEntity) =
            ReviewResponse(
                nickname = reviewEntity.userEntity.nickname,
                sex = reviewEntity.userEntity.sex,
                height = reviewEntity.userEntity.height,
                weight = reviewEntity.userEntity.weight,
                itemName = reviewEntity.itemEntity.name,
                optionName = reviewEntity.purchaseEntity.optionName,
                imageUrl = reviewEntity.itemEntity.imageUrl,
                rating = reviewEntity.rating,
                text = reviewEntity.text,
                size = reviewEntity.size,
                color = reviewEntity.color,
            )
    }
}
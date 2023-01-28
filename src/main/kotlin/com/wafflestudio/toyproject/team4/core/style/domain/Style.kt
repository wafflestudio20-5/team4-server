package com.wafflestudio.toyproject.team4.core.style.domain

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.item.domain.RankingItem
import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity
import com.wafflestudio.toyproject.team4.core.user.domain.User
import java.time.LocalDateTime

data class Style(
    val id: Long,
    val user: User,
    val images: List<String>,
    val items: List<RankingItem>,
    val content: String?,
    val hashtag: String?,
    val createdDateTime: LocalDateTime,
) {
    companion object {
        fun of(
            entity: StyleEntity,
            items: List<ItemEntity>
        ) = entity.run {
            Style(
                id = id,
                user = User.of(user),
                images = listOfNotNull(image1, image2, image3, image4, image5),
                items = items.map { entity -> RankingItem.of(entity) },
                content = content,
                hashtag = hashtag,
                createdDateTime = createdDateTime
            )
        }
    }
}

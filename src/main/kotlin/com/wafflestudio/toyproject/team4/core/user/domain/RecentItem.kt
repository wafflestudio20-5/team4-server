package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.database.RecentItemEntity
import java.time.LocalDateTime

data class RecentItem(
    val id: Long,
    val item: Item,
    val viewedDateTime: LocalDateTime,
) {
    companion object {
        fun of(recentItemEntity: RecentItemEntity) = recentItemEntity.run {
            RecentItem(
                id = id,
                item = Item.of(item),
                viewedDateTime = viewedDateTime,
            )
        }
    }
}
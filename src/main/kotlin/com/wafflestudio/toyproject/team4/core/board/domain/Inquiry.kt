package com.wafflestudio.toyproject.team4.core.board.domain

import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.domain.User
import java.time.LocalDateTime

data class Inquiry(
    val id: Long,
    val user: User,
    val item: Item,
    val isAnswered: Boolean,
    val type: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val isSecret: Boolean,
    val createdDateTime: LocalDateTime,
    val modifiedDateTime: LocalDateTime,
) {
    companion object {
        fun of(entity: InquiryEntity) = entity.run {
            Inquiry(
                id = id,
                user = User.of(this.user),
                item = Item.of(this.item),
                isAnswered = isAnswered,
                type = type.toString().lowercase(),
                title = title,
                content = content,
                images = images.map { it.imageUrl },
                isSecret = isSecret,
                createdDateTime = createdDateTime,
                modifiedDateTime = modifiedDateTime
            )
        }
    }
}


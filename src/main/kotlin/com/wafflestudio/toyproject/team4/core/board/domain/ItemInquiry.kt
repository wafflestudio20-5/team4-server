package com.wafflestudio.toyproject.team4.core.board.domain

import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import java.time.LocalDateTime

data class ItemInquiry(
    val id: Long,
    val username: String,
    val itemName: String,
    val isAnswered: Boolean,
    val type: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val isSecret: Boolean,
    val createdDateTime: LocalDateTime,
) {
    companion object {
        fun of(entity: InquiryEntity) = entity.run {
            ItemInquiry(
                id = id,
                username = this.user.username,
                itemName = this.item.name,
                isAnswered = isAnswered,
                type = type.toString().lowercase(),
                title = title,
                content = content,
                images = images.map { it.imageUrl },
                isSecret = isSecret,
                createdDateTime = createdDateTime,
            )
        }
    }
}

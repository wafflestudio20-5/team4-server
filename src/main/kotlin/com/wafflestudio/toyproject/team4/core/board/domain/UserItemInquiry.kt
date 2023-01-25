package com.wafflestudio.toyproject.team4.core.board.domain

import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import com.wafflestudio.toyproject.team4.core.item.domain.InquiryItem
import java.time.LocalDateTime

data class UserItemInquiry(
    val id: Long,
    val item: InquiryItem,
    val isAnswered: Boolean,
    val type: String,
    val title: String,
    val content: String,
    val option: String?,
    val images: List<String>,
    val isSecret: Boolean,
    val createdDateTime: LocalDateTime,
    val modifiedDateTime: LocalDateTime,
) {
    companion object {
        fun of(entity: InquiryEntity) = entity.run {
            UserItemInquiry(
                id = id,
                item = InquiryItem.of(this.item),
                isAnswered = isAnswered,
                type = type.toString().lowercase(),
                title = title,
                content = content,
                option = optionName,
                images = images.map { it.imageUrl },
                isSecret = isSecret,
                createdDateTime = createdDateTime,
                modifiedDateTime = modifiedDateTime
            )
        }
    }
}

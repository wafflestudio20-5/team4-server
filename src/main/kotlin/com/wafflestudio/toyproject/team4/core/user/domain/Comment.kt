package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.user.database.CommentEntity
import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val username: String,
    val text: String,
    val date: LocalDateTime,
) {
    companion object {
        fun of(commentEntity: CommentEntity) =
            Comment(
                id = commentEntity.id,
                username = commentEntity.userEntity.username,
                text = commentEntity.text,
                date = commentEntity.date,
            )
    }
}
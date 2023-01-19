package com.wafflestudio.toyproject.team4.core.board.domain

import com.wafflestudio.toyproject.team4.core.board.database.CommentEntity
import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val content: String,
    val createdDateTime: LocalDateTime,
) {
    companion object {
        fun of(commentEntity: CommentEntity) = commentEntity.run {
            Comment(
                id = id,
                content = content,
                createdDateTime = createdDateTime,
            )
        }
    }
}
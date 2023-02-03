package com.wafflestudio.toyproject.team4.core.board.domain

import com.wafflestudio.toyproject.team4.core.board.database.CommentEntity
import com.wafflestudio.toyproject.team4.core.user.domain.User
import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val user: User,
    val content: String,
    val createdDateTime: LocalDateTime,
    val modifiedDateTime: LocalDateTime,
) {
    companion object {
        fun of(commentEntity: CommentEntity) = commentEntity.run {
            Comment(
                id = id,
                user = User.of(user),
                content = content,
                createdDateTime = createdDateTime,
                modifiedDateTime = modifiedDateTime
            )
        }
    }
}

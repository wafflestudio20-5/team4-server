package com.wafflestudio.toyproject.team4.core.board.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.board.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface CommentRepository : JpaRepository<CommentEntity, Long>, CommentRepositoryCustom

interface CommentRepositoryCustom {
    fun getCommentResponses(commentEntities: MutableList<CommentEntity>): MutableList<Comment>
}

@Component
class CommentRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : CommentRepositoryCustom {

    override fun getCommentResponses(commentEntities: MutableList<CommentEntity>): MutableList<Comment> {
        var result = mutableListOf<Comment>()
        for (commentEntity in commentEntities) {
            result.add(Comment.of(commentEntity))
        }
        return result
    }
}

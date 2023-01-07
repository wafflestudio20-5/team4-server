package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.user.api.response.ReviewResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ReviewRepository : JpaRepository<ReviewEntity, Long>, ReviewRepositoryCustom {
}

interface ReviewRepositoryCustom {
    fun getReviewResponses(reviewEntities: MutableList<ReviewEntity>): MutableList<ReviewResponse>
}

@Component
class ReviewRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
    private val commentRepository: CommentRepository,
) : ReviewRepositoryCustom {

    override fun getReviewResponses(reviewEntities: MutableList<ReviewEntity>): MutableList<ReviewResponse> {
        val result = mutableListOf<ReviewResponse>()
        for (reviewEntity in reviewEntities) {
            val reviewResponse = ReviewResponse.of(reviewEntity)
            reviewResponse.comments = commentRepository.getCommentResponses(reviewEntity.commentEntities)
            result.add(reviewResponse)
        }
        return result
    }
}

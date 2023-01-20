package com.wafflestudio.toyproject.team4.core.board.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.board.database.QReviewEntity.reviewEntity
import com.wafflestudio.toyproject.team4.core.user.database.QPurchaseEntity.purchaseEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ReviewRepository : JpaRepository<ReviewEntity, Long>, ReviewRepositoryCustom {
    fun findAllByUser(user: UserEntity): List<ReviewEntity>
}

interface ReviewRepositoryCustom {
    fun findAllByItemIdOrderByRatingDesc(itemId: Long): List<ReviewEntity>
}

@Component
class ReviewRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ReviewRepositoryCustom {
    override fun findAllByItemIdOrderByRatingDesc(itemId: Long): List<ReviewEntity> {
        return queryFactory
            .select(reviewEntity)
            .from(reviewEntity)
            .leftJoin(purchaseEntity)
            .on(purchaseEntity.review.eq(reviewEntity))
            .fetchJoin()
            .where(purchaseEntity.item.id.eq(itemId))
            .orderBy(reviewEntity.rating.desc())
            .fetch()
    }
}

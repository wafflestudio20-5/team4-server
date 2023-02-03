package com.wafflestudio.toyproject.team4.core.board.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.board.database.QReviewEntity.reviewEntity
import com.wafflestudio.toyproject.team4.core.purchase.database.QPurchaseEntity.purchaseEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ReviewRepository : JpaRepository<ReviewEntity, Long>, ReviewRepositoryCustom {
    fun findAllByUser(user: UserEntity): List<ReviewEntity>
}

interface ReviewRepositoryCustom {
    fun findAllByItemIdOrderByRatingDesc(itemId: Long, index: Long, count: Long): List<ReviewEntity>
}

@Component
class ReviewRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ReviewRepositoryCustom {
    override fun findAllByItemIdOrderByRatingDesc(itemId: Long, index: Long, count: Long): List<ReviewEntity> {
        val reviewIds = queryFactory
            .select(reviewEntity.id)
            .from(reviewEntity)
            .where(reviewEntity.purchase.item.id.eq(itemId))
            .orderBy(reviewEntity.createdDateTime.desc(), reviewEntity.rating.desc())
            .offset(index * count)
            .limit(count)
            .fetch()

        return queryFactory
            .selectDistinct(reviewEntity)
            .from(reviewEntity)
            .leftJoin(reviewEntity.purchase, purchaseEntity).fetchJoin()
            .where(reviewEntity.id.`in`(reviewIds))
            .orderBy(reviewEntity.createdDateTime.desc(), reviewEntity.rating.desc())
            .fetch()
    }
}

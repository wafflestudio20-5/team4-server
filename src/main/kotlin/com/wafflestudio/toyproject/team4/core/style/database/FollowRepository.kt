package com.wafflestudio.toyproject.team4.core.style.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.style.database.QFollowEntity.followEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface FollowRepository : JpaRepository<FollowEntity, Long>, FollowRepositoryCustom

interface FollowRepositoryCustom {
    fun findRelation(followerId: Long, followingId: Long): Boolean
}

@Component
class FollowRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FollowRepositoryCustom {
    override fun findRelation(followerId: Long, followingId: Long): Boolean {
        val relation = queryFactory
            .select(followEntity)
            .from(followEntity)
            .where(
                followEntity.followingId.eq(followingId),
                followEntity.followerId.eq(followerId),
                followEntity.isActive.eq(true)
            ).fetchOne()
        return relation != null
    }
}

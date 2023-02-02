package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.style.database.QFollowEntity.followEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface FollowRepository : JpaRepository<FollowEntity, Long>, FollowRepositoryCustom

interface FollowRepositoryCustom {
    fun findRelation(followingUser: UserEntity, followedUser: UserEntity): FollowEntity?
}

@Component
class FollowRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FollowRepositoryCustom {
    override fun findRelation(followingUser: UserEntity, followedUser: UserEntity): FollowEntity? {
        return queryFactory
            .select(followEntity)
            .from(followEntity)
            .where(
                followEntity.following.eq(followingUser),
                followEntity.followed.eq(followedUser)
            ).fetchOne()
    }
}

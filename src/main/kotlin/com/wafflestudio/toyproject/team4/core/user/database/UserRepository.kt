package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.user.database.QUserEntity.userEntity
import com.wafflestudio.toyproject.team4.core.style.database.QFollowEntity.followEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    fun findByUsername(username: String): UserEntity?

    fun findByNickname(nickname: String): UserEntity?
}

interface UserRepositoryCustom {
    fun findByIdOrNullWithStylesAndFollows(userId: Long): UserEntity?

    fun findByIdOrNullWithFollowersWithUsers(userId: Long): UserEntity?

    fun findByIdOrNullWithFollowingsWithUsers(userId: Long): UserEntity?

    fun findByUsernameOrNullWithFollows(username: String): UserEntity?
}

@Component
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun findByIdOrNullWithStylesAndFollows(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.styles).fetchJoin()
            .leftJoin(userEntity.followings).fetchJoin()
            .leftJoin(userEntity.followers).fetchJoin()
            .where(userEntity.id.eq(userId))
            .fetchOne()
    }

    override fun findByIdOrNullWithFollowersWithUsers(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.followers, followEntity).fetchJoin()
            .leftJoin(followEntity.following).fetchJoin()
            .where(userEntity.id.eq(userId), followEntity.isActive.eq(true))
            .fetchOne()
    }

    override fun findByIdOrNullWithFollowingsWithUsers(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.followings, followEntity).fetchJoin()
            .leftJoin(followEntity.followed).fetchJoin()
            .where(userEntity.id.eq(userId), followEntity.isActive.eq(true))
            .fetchOne()
    }

    override fun findByUsernameOrNullWithFollows(username: String): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.followings).fetchJoin()
            .leftJoin(userEntity.followers).fetchJoin()
            .where(userEntity.username.eq(username))
            .fetchOne()
    }
}

package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.board.database.QReviewEntity.reviewEntity
import com.wafflestudio.toyproject.team4.core.user.database.QUserEntity.userEntity
import com.wafflestudio.toyproject.team4.core.purchase.database.QPurchaseEntity.purchaseEntity
import com.wafflestudio.toyproject.team4.core.style.database.QStyleEntity.styleEntity
import com.wafflestudio.toyproject.team4.core.user.database.QFollowEntity.followEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    fun findByUsername(username: String): UserEntity?
    fun findByNickname(nickname: String): UserEntity?
}

interface UserRepositoryCustom {
    fun findByIdOrNullWithStylesOrderByRecentDesc(userId: Long): UserEntity?
    fun findByIdOrNullWithStyles(userId: Long): UserEntity?
    fun findByIdOrNullWithFollowersWithUsers(userId: Long): UserEntity?
    fun findByIdOrNullWithFollowingsWithUsers(userId: Long): UserEntity?
    fun findByUsernameOrNullWithFollows(username: String): UserEntity?
    fun searchByQueryOrderByFollowers(query: String, index: Long, count: Long): List<UserEntity>
    fun findByUsernameFetchJoinPurchases(username: String): UserEntity?
    fun findByUsernameFetchJoinCartItems(username: String): UserEntity?
    fun findByUsernameWithReviews(username: String): UserEntity?
}

@Component
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun findByIdOrNullWithStylesOrderByRecentDesc(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .where(userEntity.id.eq(userId))
            .leftJoin(userEntity.styles, styleEntity).fetchJoin()
            .orderBy(styleEntity.createdDateTime.desc())
            .fetchOne()
    }

    override fun findByIdOrNullWithStyles(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.styles).fetchJoin()
            .where(userEntity.id.eq(userId))
            .fetchOne()
    }

    override fun findByIdOrNullWithFollowersWithUsers(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.followers, followEntity).fetchJoin()
            .leftJoin(followEntity.following).fetchJoin()
            .where(userEntity.id.eq(userId))
            .fetchOne()
    }

    override fun findByIdOrNullWithFollowingsWithUsers(userId: Long): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.followings, followEntity).fetchJoin()
            .leftJoin(followEntity.followed).fetchJoin()
            .where(userEntity.id.eq(userId))
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

    override fun searchByQueryOrderByFollowers(query: String, index: Long, count: Long): List<UserEntity> {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .where(if (query.isBlank()) null else userEntity.nickname.contains(query))
            .orderBy(userEntity.followerCount.desc())
            .offset(count * index)
            .limit(count)
            .fetch()
    }

    override fun findByUsernameFetchJoinPurchases(username: String): UserEntity? {
        return queryFactory
            .select(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.purchases, purchaseEntity).fetchJoin()
            .orderBy(
                purchaseEntity.review.createdDateTime.desc().nullsFirst(),
                purchaseEntity.createdDateTime.desc()
            )
            .where(userEntity.username.eq(username))
            .fetchFirst()
    }

    override fun findByUsernameFetchJoinCartItems(username: String): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.cartItems).fetchJoin()
            .where(userEntity.username.eq(username))
            .fetchFirst()
    }

    override fun findByUsernameWithReviews(username: String): UserEntity? {
        return queryFactory
            .select(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.reviews, reviewEntity).fetchJoin()
            .orderBy(reviewEntity.createdDateTime.desc())
            .where(userEntity.username.eq(username))
            .fetchFirst()
    }
}

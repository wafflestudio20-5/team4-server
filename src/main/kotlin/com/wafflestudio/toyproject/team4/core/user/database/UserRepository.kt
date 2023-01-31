package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.user.database.QUserEntity.userEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    fun findByUsername(username: String): UserEntity?
    fun findByNickname(nickname: String): UserEntity?
}

interface UserRepositoryCustom {
    fun findByUsernameFetchJoinPurchases(username: String): UserEntity?

}

@Component
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    override fun findByUsernameFetchJoinPurchases(username: String): UserEntity? {
        return queryFactory
            .selectDistinct(userEntity)
            .from(userEntity)
            .leftJoin(userEntity.purchases).fetchJoin()
            .fetchOne()
    }
}

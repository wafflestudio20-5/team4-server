package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ReviewRepository : JpaRepository<ReviewEntity, Long>, ReviewRepositoryCustom {
    fun findAllByUser(user: UserEntity): List<ReviewEntity>
}

interface ReviewRepositoryCustom {
}

@Component
class ReviewRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ReviewRepositoryCustom

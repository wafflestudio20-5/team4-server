package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface RecentItemRepository : JpaRepository<RecentItemEntity, Long>, RecentItemRepositoryCustom {
    fun findAllByUserOrderByViewedDateTimeDesc(user: UserEntity): List<RecentItemEntity>
}

interface RecentItemRepositoryCustom

@Component
class RecentItemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : RecentItemRepositoryCustom

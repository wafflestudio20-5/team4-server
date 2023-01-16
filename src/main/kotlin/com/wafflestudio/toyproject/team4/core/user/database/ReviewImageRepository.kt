package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ReviewImageRepository : JpaRepository<ReviewImageEntity, Long>, ReviewImageRepositoryCustom {
}

interface ReviewImageRepositoryCustom

@Component
class ReviewImageRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ReviewImageRepositoryCustom

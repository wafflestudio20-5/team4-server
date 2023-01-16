package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface PurchaseRepository : JpaRepository<PurchaseEntity, Long>, PurchaseRepositoryCustom {
    fun findAllByUser(userEntity: UserEntity): List<PurchaseEntity>
}

interface PurchaseRepositoryCustom {
}

@Component
class PurchaseRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : PurchaseRepositoryCustom {
}

package com.wafflestudio.toyproject.team4.core.purchase.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface CartItemRepository : JpaRepository<CartItemEntity, Long>, CartItemRepositoryCustom {
    fun findAllByUser(userEntity: UserEntity): List<CartItemEntity>
}

interface CartItemRepositoryCustom

@Component
class CartItemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : CartItemRepositoryCustom

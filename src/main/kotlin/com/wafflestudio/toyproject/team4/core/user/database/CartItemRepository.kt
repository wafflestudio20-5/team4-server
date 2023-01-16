package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
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

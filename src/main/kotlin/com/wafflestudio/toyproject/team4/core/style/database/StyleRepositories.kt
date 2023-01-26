package com.wafflestudio.toyproject.team4.core.style.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ItemStyleRepository : JpaRepository<ItemStyleEntity, Long>
interface UserLikedStyleRepository : JpaRepository<UserLikedStyleEntity, Long>

interface StyleRepository : JpaRepository<StyleEntity, Long>, StyleRepositoryCustom
interface StyleRepositoryCustom

@Component
class StyleRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : StyleRepositoryCustom
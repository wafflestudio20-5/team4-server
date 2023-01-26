package com.wafflestudio.toyproject.team4.core.style.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.style.database.QStyleEntity.styleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ItemStyleRepository : JpaRepository<ItemStyleEntity, Long>
interface UserLikedStyleRepository : JpaRepository<UserLikedStyleEntity, Long>

interface StyleRepository : JpaRepository<StyleEntity, Long>, StyleRepositoryCustom
interface StyleRepositoryCustom {
    fun findAllOrderBy(sort: StyleRepositoryCustomImpl.Sort): List<StyleEntity>
}

@Component
class StyleRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : StyleRepositoryCustom {
    override fun findAllOrderBy(sort: Sort): List<StyleEntity> {
        val ordering = when (sort) {
            Sort.RECENT -> styleEntity.createdDateTime.desc()
            else -> styleEntity.likedUserCount.desc()
        }

        return queryFactory
            .selectDistinct(styleEntity)
            .from(styleEntity)
            .leftJoin(styleEntity.styleItems).fetchJoin()
            .orderBy(ordering)
            .fetch()
    }

    enum class Sort {
        RECENT, LIKE
    }
}
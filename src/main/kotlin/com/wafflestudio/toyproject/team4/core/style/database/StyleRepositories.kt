package com.wafflestudio.toyproject.team4.core.style.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.style.database.QStyleEntity.styleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ItemStyleRepository : JpaRepository<ItemStyleEntity, Long>
interface UserLikedStyleRepository : JpaRepository<UserLikedStyleEntity, Long>

interface StyleRepository : JpaRepository<StyleEntity, Long>, StyleRepositoryCustom
interface StyleRepositoryCustom {
    fun findAllOrderBy(index: Long, count: Long, sort: StyleRepositoryCustomImpl.Sort): List<StyleEntity>

    fun getTotalCount(): Long
}

@Component
class StyleRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : StyleRepositoryCustom {
    override fun findAllOrderBy(index: Long, count: Long, sort: Sort): List<StyleEntity> {
        val ordering = when (sort) {
            Sort.RECENT -> styleEntity.createdDateTime.desc()
            else -> styleEntity.likedUserCount.desc()
        }

        val styleIds = queryFactory
            .select(styleEntity.id)
            .from(styleEntity)
            .orderBy(ordering, styleEntity.createdDateTime.desc())
            .offset(index * count)
            .limit(count)
            .fetch()

        return queryFactory
            .selectDistinct(styleEntity)
            .from(styleEntity)
            .leftJoin(styleEntity.styleItems).fetchJoin()
            .orderBy(ordering, styleEntity.createdDateTime.desc())
            .where(styleEntity.id.`in`(styleIds))
            .fetch()
    }

    override fun getTotalCount(): Long {
        return queryFactory
            .select(styleEntity.count())
            .from(styleEntity)
            .fetchOne()!!
    }

    enum class Sort {
        RECENT, LIKE
    }
}

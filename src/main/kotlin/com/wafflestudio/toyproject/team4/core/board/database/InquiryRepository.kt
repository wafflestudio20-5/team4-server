package com.wafflestudio.toyproject.team4.core.board.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.board.database.QInquiryEntity.inquiryEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InquiryRepository : JpaRepository<InquiryEntity, Long>, InquiryRepositoryCustom

interface InquiryRepositoryCustom {
    fun findAllByUserOrderByCreatedDateTimeDesc(user: UserEntity, index: Long, count: Long): List<InquiryEntity>
    fun findAllByItem_IdOrderByCreatedDateTimeDesc(itemId: Long, index: Long, count: Long): List<InquiryEntity>
    fun getItemTotalInquiryCount(itemId: Long): Long
}

@Component
class InquiryRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : InquiryRepositoryCustom {
    override fun findAllByUserOrderByCreatedDateTimeDesc(user: UserEntity, index: Long, count: Long): List<InquiryEntity> {
        return queryFactory
            .select(inquiryEntity)
            .from(inquiryEntity)
            .where(inquiryEntity.user.eq(user))
            .orderBy(inquiryEntity.createdDateTime.desc())
            .offset(count*index)
            .limit(count)
            .fetch()
    }

    override fun findAllByItem_IdOrderByCreatedDateTimeDesc(itemId: Long, index: Long, count: Long): List<InquiryEntity> {
        return queryFactory
            .select(inquiryEntity)
            .from(inquiryEntity)
            .where(inquiryEntity.item.id.eq(itemId))
            .orderBy(inquiryEntity.createdDateTime.desc())
            .offset(count*index)
            .limit(count)
            .fetch()
    }

    override fun getItemTotalInquiryCount(itemId: Long): Long {
        return queryFactory
            .select(inquiryEntity.count())
            .from(inquiryEntity)
            .where(inquiryEntity.item.id.eq(itemId))
            .fetchOne()!!
    }
}

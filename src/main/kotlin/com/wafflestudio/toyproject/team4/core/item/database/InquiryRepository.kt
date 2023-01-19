package com.wafflestudio.toyproject.team4.core.item.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InquiryRepository: JpaRepository<InquiryEntity, Long> {
    fun findAllByItem_IdOrderByCreatedDateTimeDesc(itemId: Long): List<InquiryEntity>
}

interface InquiryRepositoryCustom

@Component
class InquiryRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : InquiryRepositoryCustom
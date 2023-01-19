package com.wafflestudio.toyproject.team4.core.board.database

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InquiryImageRepository : JpaRepository<InquiryImageEntity, Long>, InquiryImageRepositoryCustom {
    fun findAllByInquiry_Id(inquiryId: Long): List<InquiryImageEntity>
}

interface InquiryImageRepositoryCustom

@Component
class InquiryImageRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : InquiryImageRepositoryCustom

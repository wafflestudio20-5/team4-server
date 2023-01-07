package com.wafflestudio.toyproject.team4.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.user.api.response.PurchaseResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface PurchaseRepository : JpaRepository<PurchaseEntity, Long>, PurchaseRepositoryCustom {
}

interface PurchaseRepositoryCustom {
    fun getPurchaseResponses(purchaseEntities: MutableList<PurchaseEntity>): MutableList<PurchaseResponse>
}

@Component
class PurchaseRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : PurchaseRepositoryCustom {

    override fun getPurchaseResponses(purchaseEntities: MutableList<PurchaseEntity>): MutableList<PurchaseResponse> {
        val result = mutableListOf<PurchaseResponse>()
        for (purchaseEntity in purchaseEntities) {
            result.add(PurchaseResponse.of(purchaseEntity))
        }
        return result
    }
}

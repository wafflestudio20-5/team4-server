package com.wafflestudio.toyproject.team4.core.purchase.api.response

import com.wafflestudio.toyproject.team4.core.purchase.domain.Purchase

data class PurchaseItemsResponse(
    val purchaseItems: List<Purchase>
)

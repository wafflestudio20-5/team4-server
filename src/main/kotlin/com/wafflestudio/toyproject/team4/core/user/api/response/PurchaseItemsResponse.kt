package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.Purchase

data class PurchaseItemsResponse(
    val purchaseItems: List<Purchase>
) 
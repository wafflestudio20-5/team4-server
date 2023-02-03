package com.wafflestudio.toyproject.team4.core.purchase.api.response

import com.wafflestudio.toyproject.team4.core.purchase.domain.CartItem

data class CartItemsResponse(
    val cartItems: List<CartItem>
)

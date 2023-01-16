package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.CartItem

data class CartItemsResponse(
    val cartItems: List<CartItem>
) 
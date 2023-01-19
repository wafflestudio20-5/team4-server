package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.database.CartItemEntity

data class CartItem(
    val id: Long,
    val item: Item,
    val option: String?,
    val quantity: Long,
) {
    companion object {
        fun of(cartItemEntity: CartItemEntity) = cartItemEntity.run {
            CartItem(
                id = id,
                item = Item.of(item),
                option = optionName,
                quantity = quantity,
            )
        }
    }
}

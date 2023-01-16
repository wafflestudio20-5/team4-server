package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseEntity
import java.time.LocalDateTime

data class Purchase(
    val id: Long,
    val user: User,
    val item: Item,
    val option: String?,
    val createdDateTime: LocalDateTime,
    val payment: Long,
    val quantity: Long,
) {
    companion object {
        fun of(purchaseEntity: PurchaseEntity) = purchaseEntity.run {
            Purchase(
                id = id,
                user = User.of(user),
                item = Item.of(item),
                option = optionName,
                createdDateTime = createdDateTime,
                payment = payment,
                quantity = quantity,
            )
        }
    }
}
package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.database.PurchaseEntity
import java.time.LocalDateTime

data class PurchaseResponse(
    val brand: String,
    val itemName: String,
    val optionName: String?,
    val imageUrl: String,

    val id: Long,
    val date: LocalDateTime,
    val payment: Long,
    val quantity: Long,
) {
    companion object {
        fun of(purchaseEntity: PurchaseEntity) =
            PurchaseResponse(
                brand = purchaseEntity.itemEntity.brand,
                itemName = purchaseEntity.itemEntity.name,
                optionName = purchaseEntity.optionName,
                imageUrl = purchaseEntity.itemEntity.imageUrl,
                id = purchaseEntity.id,
                date = purchaseEntity.date,
                payment = purchaseEntity.payment,
                quantity = purchaseEntity.quantity
            )
    }
}
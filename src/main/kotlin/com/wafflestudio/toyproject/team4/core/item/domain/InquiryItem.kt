package com.wafflestudio.toyproject.team4.core.item.domain

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity

data class InquiryItem(
    val id: Long,
    val name: String,
    val brand: String,
    val image: String,
    val oldPrice: Long,
    val newPrice: Long? = null,
    val sale: Long? = null,
    val options: List<String>? = null,
) {

    companion object {
        fun of(entity: ItemEntity) = entity.run {
            InquiryItem(
                id = id,
                name = name,
                brand = brand,
                image = images[0].imageUrl,
                oldPrice = oldPrice,
                newPrice = newPrice,
                sale = sale,
                options = if (options.isNullOrEmpty()) null else options?.map { it.optionName },
            )
        }
    }
}

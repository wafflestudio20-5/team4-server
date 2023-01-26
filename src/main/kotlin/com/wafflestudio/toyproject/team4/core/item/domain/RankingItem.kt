package com.wafflestudio.toyproject.team4.core.item.domain

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import org.apache.commons.text.CaseUtils

data class RankingItem(
    val id: Long,
    val name: String,
    val brand: String,
    val image: String,
    val label: String? = null,
    val oldPrice: Long,
    val newPrice: Long? = null,
    val sale: Long? = null,
    val reviewCount: Long, // 후기 순 정렬 위함
    val rating: Double?, // 별점 순 정렬 위함
    val sex: String,
    val category: String,
    val subCategory: String,
) {

    companion object {
        fun of(entity: ItemEntity) = entity.run {
            RankingItem(
                id = id,
                name = name,
                brand = brand,
                image = images[0].imageUrl,
                label = label?.toString()?.lowercase(),
                oldPrice = oldPrice,
                newPrice = newPrice,
                sale = sale,
                reviewCount = reviewCount!!,
                rating = rating,
                sex = sex.toString().lowercase(),
                category = CaseUtils.toCamelCase(category.toString(), false, '_'),
                subCategory = CaseUtils.toCamelCase(subCategory.toString(), false, '_')
            )
        }
    }
}

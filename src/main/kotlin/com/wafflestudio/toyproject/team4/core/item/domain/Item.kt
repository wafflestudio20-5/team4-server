package com.wafflestudio.toyproject.team4.core.item.domain

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import org.apache.commons.text.CaseUtils

data class Item(
    val id: Long,
    val name: String,
    val brand: String,
    val images: List<String>,
    val label: String? = null,
    val oldPrice: Long,
    val newPrice: Long? = null,
    val sale: Long? = null,
    val sex: String,
    val reviewCount: Long,
    val rating: Double?,
    val options: List<String>? = null,
    val category: String,
    val subCategory: String,
) {
    
    enum class Label {
        LIMITED, BOUTIQUE, PREORDER, EXCLUSIVE
    }
    
    enum class Sex {
        MALE, FEMALE, BOTH
    }
    
    enum class Category {
        TOP, OUTER, PANTS, SKIRT, BAG, SHOES, HEAD_WEAR
    }

    enum class SubCategory {
        SWEATER, HOODIE, SWEAT_SHIRT, SHIRT,  // TOP
        COAT, JACKET, PADDING, CARDIGAN,     // OUTER
        DENIM, SLACKS, JOGGER, LEGGINGS,     // PANTS
        MINI_SKIRT, MEDI_SKIRT, LONG_SKIRT,     // SKIRT
        BACKPACK, CROSS_BAG, ECHO_BAG,         // BAG
        GOODOO, SANDAL, SLIPPER, SNEAKERS,   // SHOES
        CAP, HAT, BEANIE                     // HEADWEAR 
    }
    
    
    companion object {
        fun of(entity: ItemEntity): Item = entity.run {
            Item(
                id = id,
                name = name,
                brand = brand,
                images = images.map { it.imageUrl },
                label = label?.toString()?.lowercase(),
                oldPrice = oldPrice,
                newPrice = newPrice,
                sale = sale,
                sex = sex.toString().lowercase(),
                reviewCount = reviewCount!!,
                rating = rating,
                options = if(options.isNullOrEmpty()) null else options?.map{ it.optionName },
                category = CaseUtils.toCamelCase(category.toString(), false, '_'),
                subCategory = CaseUtils.toCamelCase(subCategory.toString(), false, '_')
            )
        }
    }
}
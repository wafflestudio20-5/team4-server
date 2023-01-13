package com.wafflestudio.toyproject.team4.core.item.domain

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.item.database.OptionEntity

data class Item(
    val id: Long,
    val name: String,
    val brand: String,
    val image: String,
    val label: String?,
    val oldPrice: Long,
    val newPrice: Long?,
    val sale: Long?,
    val sex: String,
    val rating: Double?,
    val options: MutableList<OptionEntity>? = mutableListOf(),
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
        TOP, OUTER, PANTS, SKIRT, BAG, SHOES, HEADWEAR
    }

    enum class SubCategory {
        SWEATER, HOODIE, SWEATSHIRT, SHIRT,  // TOP
        COAT, JACKET, PADDING, CARDIGAN,     // OUTER
        DENIM, SLACKS, JOGGER, LEGGINGS,     // PANTS
        MINISKIRT, MEDISKIRT, LONGSKIRT,     // SKIRT
        BACKPACK, CROSSBAG, ECHOBAG,         // BAG
        GOODOO, SANDAL, SLIPPER, SNEAKERS,   // SHOES
        CAP, HAT, BEANIE                     // HEADWEAR 
    }
    
    
    companion object {
        fun of(entity: ItemEntity): Item = entity.run {
            Item(
                id = id,
                name = name,
                brand = brand,
                image = image,
                label = label.toString().lowercase(),
                oldPrice = oldPrice,
                newPrice = newPrice,
                sale = sale!!,
                sex = sex.toString().lowercase(),
                rating = rating,
                options = options,
                category = category.toString().lowercase(),
                subCategory = subCategory.toString().lowercase()
            )
        }
    }
}
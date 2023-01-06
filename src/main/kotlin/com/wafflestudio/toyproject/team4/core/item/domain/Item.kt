package com.wafflestudio.toyproject.team4.core.item.domain

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity

data class Item (
    val id: Long,
    val name: String,
    val brand: String,
    val imageUrl: String,
    val label: String?,
    val oldPrice: Long,
    val newPrice: Long,
    val sale: Long,
) {
    
    enum class Label {
        LIMITED, BOUTIQUE, PREORDER, EXCLUSIVE
    }
    
    enum class Sex {
        MALE, FEMALE, UNISEX
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
                imageUrl = imageUrl,
                label = label.toString(),
                oldPrice = oldPrice,
                newPrice = newPrice,
                sale = sale!!
            )
        }
    }
}
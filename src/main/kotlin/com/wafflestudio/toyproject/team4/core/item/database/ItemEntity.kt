package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import javax.persistence.*

@Entity
@Table(name = "items")
class ItemEntity(
    val name: String,
    val brand: String,
    val image: String,

    @Enumerated(EnumType.STRING)
    val label: Item.Label? = null,
    @Enumerated(EnumType.STRING)
    val sex: Item.Sex,
    val rating: Long? = 0L,

    val oldPrice: Long,
    var newPrice: Long,
    var sale: Long? = 0L,

    @OneToMany(
        mappedBy = "item",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val options: MutableList<OptionEntity>? = mutableListOf(),

    @Enumerated(EnumType.STRING)
    val category: Item.Category,
    @Enumerated(EnumType.STRING)
    val subCategory: Item.SubCategory,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

}
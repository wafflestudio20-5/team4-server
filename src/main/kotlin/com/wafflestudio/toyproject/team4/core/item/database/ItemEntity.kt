package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import javax.persistence.*

@Entity
@Table(name = "items")
class ItemEntity(
    val name: String,
    val brand: String,
    val imageUrl: String,

    @Enumerated(EnumType.STRING)
    val label: Item.Label? = null,
    @Enumerated(EnumType.STRING)
    val sex: Item.Sex,
    val rating: Long? = 0L,

    val oldPrice: Long,
    var sale: Long? = 0L,

    @ElementCollection(fetch=FetchType.LAZY) 
    val options: List<String>,
    
    @Enumerated(EnumType.STRING)
    val category: Item.Category,
    @Enumerated(EnumType.STRING)
    val subCategory: Item.SubCategory,

    //"reviews": Review[],    # 구매후기
    
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    var newPrice: Long = (oldPrice * (1- sale!!) + 5) / 10 * 10
    val nextItemId: Long = id + 10

}
package com.wafflestudio.toyproject.team4.core.item.database

import javax.persistence.*

@Entity
@Table(name = "item_images")
class ItemImageEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,
    
    var imageUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
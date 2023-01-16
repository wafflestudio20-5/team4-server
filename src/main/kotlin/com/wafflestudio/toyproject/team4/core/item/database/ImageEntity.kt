package com.wafflestudio.toyproject.team4.core.item.database

import javax.persistence.*

@Entity
@Table(name = "item_images")
class ImageEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,
    
    var cloudinaryUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
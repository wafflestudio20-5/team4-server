package com.wafflestudio.toyproject.team4.core.item.database

import javax.persistence.*

@Entity
@Table(name = "options")
class OptionEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,
    
    val optionName: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
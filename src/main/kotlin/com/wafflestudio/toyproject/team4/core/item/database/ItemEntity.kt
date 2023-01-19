package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.database.ReviewEntity
import javax.persistence.*

@Entity
@Table(name = "items")
class ItemEntity(
    val name: String,
    val brand: String,

    @Enumerated(EnumType.STRING)
    val label: Item.Label? = null,
    @Enumerated(EnumType.STRING)
    val sex: Item.Sex,
    var reviewCount: Long? = 0,
    var rating: Double? = 0.0,

    val oldPrice: Long,
    var newPrice: Long? = null,
    var sale: Long? = null,
    
    @Enumerated(EnumType.STRING)
    val category: Item.Category,
    @Enumerated(EnumType.STRING)
    val subCategory: Item.SubCategory,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<ItemImageEntity> = mutableListOf()

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var options: MutableList<OptionEntity>? = null


    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableList<ReviewEntity>? = null

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var inquiries: MutableList<InquiryEntity>? = null
    

    fun updateImages(
        imageUrlList: List<String>
    ) {
        this.images = imageUrlList.map {
            url -> ItemImageEntity(this, url)
        }.toMutableList()
    }
    
    fun updateOptionList(
        optionList: List<String>
    ) {
        this.options = optionList.map {
            option -> OptionEntity(this, option)
        }.toMutableList()
    }
    
}
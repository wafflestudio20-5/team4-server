package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import com.wafflestudio.toyproject.team4.core.board.api.request.PostItemInquiryRequest
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import kotlin.math.roundToInt

@Entity
@Table(name = "items")
class ItemEntity(
    val name: String,
    val brand: String,

    @Enumerated(EnumType.STRING)
    val label: Item.Label? = null,
    @Enumerated(EnumType.STRING)
    val sex: Item.Sex,
    var reviewCount: Long = 0,
    var rating: Double = 0.0,
    var ratingSum: Double = 0.0,

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
    var options: MutableList<ItemOptionEntity>? = null

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var inquiries: MutableList<InquiryEntity> = mutableListOf()

    fun updateImages(imageUrlList: List<String>) {
        this.images = imageUrlList.map { url ->
            ItemImageEntity(this, url)
        }.toMutableList()
    }

    fun updateOptionList(optionList: List<String>) {
        this.options = optionList.map { option ->
            ItemOptionEntity(this, option)
        }.toMutableList()
    }

    fun writeInquiry(writer: UserEntity, request: PostItemInquiryRequest) {
        val newInquiry = request.toEntity(writer, this)
        this.inquiries.add(newInquiry)
    }

    fun addRating(rating: Long) {
        this.ratingSum += rating
        this.recalculateRating()
    }

    fun changeRating(originalRating: Long, newRating: Long) {
        this.ratingSum = this.ratingSum - originalRating + newRating
        this.recalculateRating()
    }

    fun deleteRating(rating: Long) {
        this.ratingSum -= rating
        if (reviewCount == 0L)
            this.rating = 0.0
        else
            this.recalculateRating()
    }

    fun recalculateRating() {
        this.rating = ((this.ratingSum / this.reviewCount) * 10).roundToInt() / 10.0
    }
}

package com.wafflestudio.toyproject.team4.core.purchase.database

import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity.Size
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity.Color
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.board.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "purchases")
@EntityListeners(AuditingEntityListener::class)
class PurchaseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,

    val optionName: String?,
    val payment: Long,
    val quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    @OneToOne(mappedBy = "purchase", cascade = [CascadeType.ALL], orphanRemoval = true)
    var review: ReviewEntity? = null

    fun writeReview(request: ReviewRequest) {
        this.review = ReviewEntity(
            user = this.user,
            purchase = this,
            rating = request.rating,
            content = request.content,
            image1 = request.images.getOrNull(0),
            image2 = request.images.getOrNull(1),
            image3 = request.images.getOrNull(2),
            size = Size.valueOf(request.size.uppercase()),
            color = Color.valueOf(request.color.uppercase()),
        )
        this.item.reviewCount++
        this.item.addRating(request.rating)
        this.user.reviewCount++
    }

    fun deleteReview(rating: Long) {
        this.item.reviewCount--
        this.item.deleteRating(rating)
        this.user.reviewCount--
        this.review = null
    }
}

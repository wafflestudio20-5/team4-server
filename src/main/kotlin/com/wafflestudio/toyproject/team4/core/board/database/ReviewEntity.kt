package com.wafflestudio.toyproject.team4.core.board.database

import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener::class)
class ReviewEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: UserEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseId")
    val purchase: PurchaseEntity,

    var rating: Long,
    var content: String,

    var image1: String? = null,
    var image2: String? = null,
    var image3: String? = null,

    var size: Size,
    var color: Color,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<CommentEntity> = mutableListOf()

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<ReviewImageEntity> = mutableListOf()

    fun addComment(user: UserEntity, content: String) {
        val comment = CommentEntity(this, user, content)
        this.comments.add(comment)
    }

    fun update(
        request: ReviewRequest
    ) {
        this.rating = request.rating
        this.content = request.content
        this.image1 = request.images.getOrNull(0)
        this.image2 = request.images.getOrNull(1)
        this.image3 = request.images.getOrNull(2)
        this.size = Size.valueOf(request.size.uppercase())
        this.color = Color.valueOf(request.color.uppercase())
    }

    enum class Size {
        LARGE, MID, SMALL,
    }

    enum class Color {
        BRIGHT, MID, DIM,
    }
}

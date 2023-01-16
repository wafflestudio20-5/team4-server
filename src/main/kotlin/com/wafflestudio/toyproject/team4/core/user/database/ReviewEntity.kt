package com.wafflestudio.toyproject.team4.core.user.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener::class)
class ReviewEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseId")
    val purchase: PurchaseEntity,

    var rating: Long,
    var content: String,
    val size: Size,
    val color: Color,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<CommentEntity> = mutableListOf()

    @OneToMany(mappedBy = "review", fetch=FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<ReviewImageEntity> = mutableListOf()
}

enum class Size {
    LARGE, MID, SMALL,
}

enum class Color {
    BRIGHT, MID, DIM,
}
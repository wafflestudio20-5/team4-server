package com.wafflestudio.toyproject.team4.core.board.database

import javax.persistence.*

@Entity
@Table(name = "review_images")
class ReviewImageEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId")
    val review: ReviewEntity,

    var cloudinaryUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
package com.wafflestudio.toyproject.team4.core.board.database

import javax.persistence.*

@Entity
@Table(name = "inquiry_images")
class InquiryImageEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiryId")
    val inquiry: InquiryEntity,

    var imageUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
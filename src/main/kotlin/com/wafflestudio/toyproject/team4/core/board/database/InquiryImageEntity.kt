package com.wafflestudio.toyproject.team4.core.board.database

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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

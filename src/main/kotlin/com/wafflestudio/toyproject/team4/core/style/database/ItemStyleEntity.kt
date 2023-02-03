package com.wafflestudio.toyproject.team4.core.style.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "item_styles",
    uniqueConstraints = [UniqueConstraint(columnNames = ["itemId", "style_id"])]
)
class ItemStyleEntity(
    val itemId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    val style: StyleEntity,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    val createdDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    var isActive: Boolean = true
}

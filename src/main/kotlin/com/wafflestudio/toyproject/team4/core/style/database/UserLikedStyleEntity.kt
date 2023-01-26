package com.wafflestudio.toyproject.team4.core.style.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
    name = "user_liked_styles",
    uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "liked_style_id"])]
)
class UserLikedStyleEntity(
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    val likedStyle: StyleEntity,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    val createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()
}

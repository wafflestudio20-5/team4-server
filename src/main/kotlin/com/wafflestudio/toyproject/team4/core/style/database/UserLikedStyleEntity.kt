package com.wafflestudio.toyproject.team4.core.style.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*

@Entity
@Table(
    name = "user_liked_styles",
    uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "styleId"])]
)
class UserLikedStyleEntity(
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "styleId")
    val likedStyle: StyleEntity,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    val createdDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    var isActive: Boolean = true

    fun changeActive() {
        this.isActive = !this.isActive
        this.likedStyle.likedUserCount += if (this.isActive) 1 else -1
    }
}

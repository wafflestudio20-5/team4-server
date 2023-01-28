package com.wafflestudio.toyproject.team4.core.style.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "follow")
class FollowEntity(
    val followingId: Long,
    val followerId: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    var isActive: Boolean = true
}

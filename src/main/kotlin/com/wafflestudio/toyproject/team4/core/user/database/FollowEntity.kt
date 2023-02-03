package com.wafflestudio.toyproject.team4.core.user.database

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

@Entity
@Table(name = "follow")
class FollowEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val following: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    val followed: UserEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    var isActive: Boolean = true

    fun activate() {
        this.isActive = true
    }

    fun deactivate() {
        this.isActive = false
    }
}

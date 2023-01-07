package com.wafflestudio.toyproject.team4.core.user.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener::class)
class CommentEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val userEntity: UserEntity,
    
    var text: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var date: LocalDateTime = LocalDateTime.now()
}
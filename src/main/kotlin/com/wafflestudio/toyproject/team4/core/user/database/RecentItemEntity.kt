package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "recent_items")
@EntityListeners(AuditingEntityListener::class)
class RecentItemEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,
    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var viewedDateTime: LocalDateTime = LocalDateTime.now()
}
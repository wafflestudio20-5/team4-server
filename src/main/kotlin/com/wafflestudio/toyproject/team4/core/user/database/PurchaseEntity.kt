package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.user.domain.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "purchases")
@EntityListeners(AuditingEntityListener::class)
class PurchaseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val userEntity: UserEntity,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val itemEntity: ItemEntity,
    
    val optionName: String,
    val payment: Long,
    val quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var date: LocalDateTime = LocalDateTime.now()
}
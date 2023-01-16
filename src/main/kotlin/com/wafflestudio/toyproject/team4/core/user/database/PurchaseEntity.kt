package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
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
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,

    val optionName: String?,
    val payment: Long,
    val quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "purchase", fetch=FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableList<ReviewEntity> = mutableListOf()
}
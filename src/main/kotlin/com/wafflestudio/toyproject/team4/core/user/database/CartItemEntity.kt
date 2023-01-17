package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*


@Entity
@Table(name = "cart_items")
@EntityListeners(AuditingEntityListener::class)
class CartItemEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val item: ItemEntity,

    val optionName: String? = null,
    var quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
package com.wafflestudio.toyproject.team4.core.purchase.database

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

    fun updateQuantity(quantity: Long) {
        this.quantity = quantity
    }
}

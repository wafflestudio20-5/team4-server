package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*


@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener::class)
class ReviewEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val userEntity: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    val itemEntity: ItemEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseId")
    val purchaseEntity: PurchaseEntity,
    
    var rating: Long,
    var text: String,
    val size: Size,
    val color: Color,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    @OneToMany(cascade = [CascadeType.REMOVE])
    var commentEntities: MutableList<CommentEntity> = mutableListOf()
}

enum class Size {
    BIG, NORMAL, SMALL,
}

enum class Color {
    BRIGHT, NORMAL, BLURRY,
}
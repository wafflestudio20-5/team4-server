package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PurchaseRequest
import com.wafflestudio.toyproject.team4.core.purchase.database.CartItemEntity
import com.wafflestudio.toyproject.team4.core.purchase.database.PurchaseEntity
import com.wafflestudio.toyproject.team4.core.user.domain.User
import com.wafflestudio.toyproject.team4.oauth.entity.ProviderType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    @Column(unique = true)
    val username: String,
    var encodedPassword: String,

    @Column(unique = true)
    var nickname: String,
    var image: String? = null,
    var reviewCount: Long = 0L,

    @Enumerated(EnumType.STRING)
    var sex: User.Sex? = null,
    var height: Long? = null,
    var weight: Long? = null,
    var description: String? = null,
    var instaUsername: String? = null,

    @Enumerated(EnumType.STRING)
    var socialKey: ProviderType? = null,

    @Enumerated(EnumType.STRING)
    val role: User.Role = User.Role.ROLE_USER,

    var refreshToken: String? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var registrationDate: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableList<ReviewEntity> = mutableListOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var purchases: MutableList<PurchaseEntity> = mutableListOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var cartItems: MutableList<CartItemEntity> = mutableListOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var recentItems: MutableList<RecentItemEntity> = mutableListOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var itemInquiries: MutableList<InquiryEntity> = mutableListOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var styles: MutableList<StyleEntity> = mutableListOf()

    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var followings: MutableSet<FollowEntity> = mutableSetOf()

    @OneToMany(mappedBy = "followed", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var followers: MutableSet<FollowEntity> = mutableSetOf()

    fun viewItem(item: ItemEntity) {
        val recentItem = RecentItemEntity(this, item)
        recentItems.add(recentItem)
    }

    fun update(request: PatchMeRequest, encodedPassword: String?) {
        this.image = request.image ?: this.image
        this.encodedPassword = encodedPassword ?: this.encodedPassword
        this.nickname = request.nickname ?: this.nickname
        this.sex =
            when (request.sex) {
                null -> this.sex
                "male" -> User.Sex.MALE
                "female" -> User.Sex.FEMALE
                else -> null
            }
        this.height = request.height ?: this.height
        this.weight = request.weight ?: this.weight
        this.description = request.description ?: this.description
        this.instaUsername = request.instaUsername ?: this.instaUsername
    }

    fun addToCart(orderItem: ItemEntity, cartRequestInfo: PostShoppingCartRequest) {
        val cartItem = CartItemEntity(
            user = this,
            item = orderItem,
            optionName = cartRequestInfo.option,
            quantity = cartRequestInfo.quantity
        )
        this.cartItems.add(cartItem)
    }

    fun purchase(orderItem: ItemEntity, purchaseRequestInfo: PurchaseRequest) {
        val newPurchase = PurchaseEntity(
            user = this,
            item = orderItem,
            optionName = purchaseRequestInfo.option,
            payment = purchaseRequestInfo.payment,
            quantity = purchaseRequestInfo.quantity
        )
        this.purchases.add(newPurchase)
    }
}

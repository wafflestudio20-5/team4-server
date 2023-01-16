package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.api.response.CartItemsResponse
import com.wafflestudio.toyproject.team4.core.user.api.response.PurchaseItemsResponse
import com.wafflestudio.toyproject.team4.core.user.api.response.ReviewsResponse
import com.wafflestudio.toyproject.team4.core.user.api.response.UserResponse
import com.wafflestudio.toyproject.team4.core.user.database.CartItemRepository
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseRepository
import com.wafflestudio.toyproject.team4.core.user.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.domain.CartItem
import com.wafflestudio.toyproject.team4.core.user.domain.Purchase
import com.wafflestudio.toyproject.team4.core.user.domain.Review
import com.wafflestudio.toyproject.team4.core.user.domain.User
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface UserService {
    fun getMe(username: String): UserResponse
    fun getReviews(username: String): ReviewsResponse
    fun getPurchases(username: String): PurchaseItemsResponse
    fun getShoppingCart(username: String): CartItemsResponse
    fun getRecentlyViewed(username: String): MutableList<Item>
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val purchaseRepository: PurchaseRepository,
    private val itemRepository: ItemRepository,
    private val cartItemRepository: CartItemRepository,
) : UserService {

    @Transactional
    override fun getMe(username: String): UserResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        return UserResponse(User.of(userEntity))
    }

    @Transactional
    override fun getReviews(username: String): ReviewsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val reviewEntities = reviewRepository.findAllByUser(userEntity)
        return ReviewsResponse(reviewEntities.map { reviewEntity -> Review.of(reviewEntity) })
    }

    @Transactional
    override fun getPurchases(username: String): PurchaseItemsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val purchaseEntities = purchaseRepository.findAllByUser(userEntity)
        return PurchaseItemsResponse(purchaseEntities.map { purchaseEntity -> Purchase.of(purchaseEntity) })
    }

    @Transactional
    override fun getShoppingCart(username: String): CartItemsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItemEntities = cartItemRepository.findAllByUser(userEntity)
        return CartItemsResponse(cartItemEntities.map { cartItemEntity -> CartItem.of(cartItemEntity) })
    }

    @Transactional
    override fun getRecentlyViewed(username: String): MutableList<Item> {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        return itemRepository.getItems(userEntity.recentlyViewed)
    }
}
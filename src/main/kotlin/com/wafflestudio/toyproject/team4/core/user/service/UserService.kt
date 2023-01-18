package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.user.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.*
import com.wafflestudio.toyproject.team4.core.user.database.*
import com.wafflestudio.toyproject.team4.core.user.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface UserService {
    fun getMe(username: String): UserResponse
    fun getReviews(username: String): ReviewsResponse
    fun getPurchases(username: String): PurchaseItemsResponse
    fun postPurchases(username: String, request: PurchasesRequest)
    fun getShoppingCart(username: String): CartItemsResponse
    fun getRecentlyViewed(username: String): RecentItemsResponse
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val purchaseRepository: PurchaseRepository,
    private val cartItemRepository: CartItemRepository,
    private val recentItemRepository: RecentItemRepository,
    private val itemRepository: ItemRepository,
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
    override fun postPurchases(username: String, request: PurchasesRequest) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        request.purchaseitems.forEach {
            val itemEntity = itemRepository.findByIdOrNull(it.id)
                ?: throw CustomHttp404("아이템 정보가 올바르지 않습니다.")
            purchaseRepository.save(
                PurchaseEntity(
                    user = userEntity,
                    item = itemEntity,
                    optionName = it.option,
                    payment = it.payment,
                    quantity = it.quantity,
                )
            )
        }
    }

    @Transactional
    override fun getShoppingCart(username: String): CartItemsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItemEntities = cartItemRepository.findAllByUser(userEntity)
        return CartItemsResponse(cartItemEntities.map { cartItemEntity -> CartItem.of(cartItemEntity) })
    }

    @Transactional
    override fun getRecentlyViewed(username: String): RecentItemsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val recentItemEntities = recentItemRepository.findAllByUser(userEntity)
        return RecentItemsResponse(recentItemEntities.map { recentItemEntity -> RecentItem.of(recentItemEntity) })
    }
}
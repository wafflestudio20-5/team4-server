package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp403
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.common.CustomHttp409
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.*
import com.wafflestudio.toyproject.team4.core.user.database.*
import com.wafflestudio.toyproject.team4.core.user.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import javax.transaction.Transactional

interface UserService {
    fun getMe(username: String): UserResponse
    fun getReviews(username: String): ReviewsResponse
    fun postReview(username: String, request: ReviewRequest)
    fun putReview(username: String, request: ReviewRequest)
    fun getPurchases(username: String): PurchaseItemsResponse
    fun getShoppingCart(username: String): CartItemsResponse
    fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest)
    fun patchShoppingCart(username: String, patchShoppingCartRequest: PatchShoppingCartRequest)
    fun deleteShoppingCart(username: String, cartItemId: Long)
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
    private val reviewImageRepository: ReviewImageRepository,
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
    override fun postReview(username: String, request: ReviewRequest) {
        val purchaseEntity = purchaseRepository.findByIdOrNull(request.id)
            ?: throw CustomHttp404("구매한 상품이 올바르지 않습니다.")
        if (request.rating < 0 || request.rating > 10)
            throw CustomHttp400("구매만족도의 범위가 올바르지 않습니다.")
        try {
            Size.valueOf(request.size.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("사이즈가 적절하지 않습니다.")
        }
        try {
            Color.valueOf(request.color.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("색감이 적절하지 않습니다.")
        }
        val reviewEntity = ReviewEntity(
            user = purchaseEntity.user,
            purchase = purchaseEntity,
            rating = request.rating,
            content = request.content,
            size = Size.valueOf(request.size.uppercase()),
            color = Color.valueOf(request.color.uppercase()),
        )
        request.images.forEach { reviewImageRepository.save(ReviewImageEntity(reviewEntity, it)) }
        reviewRepository.save(reviewEntity)
    }

    @Transactional
    override fun putReview(username: String, request: ReviewRequest) {
        if (request.rating < 0 || request.rating > 10)
            throw CustomHttp400("구매만족도의 범위가 올바르지 않습니다.")
        try {
            Size.valueOf(request.size.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("사이즈가 적절하지 않습니다.")
        }
        try {
            Color.valueOf(request.color.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("색감이 적절하지 않습니다.")
        }
        val reviewEntity = reviewRepository.findByIdOrNull(request.id)
            ?: throw CustomHttp404("존재하지 않는 구매후기입니다.")
        if (reviewEntity.user.username != username)
            throw CustomHttp403("사용자의 구매후기가 아닙니다.")
        reviewEntity.run {
            rating = request.rating
            content = request.content
            size = Size.valueOf(request.size.uppercase())
            color = Color.valueOf(request.color.uppercase())
        }
        reviewImageRepository.deleteAll(reviewEntity.images)
        request.images.forEach {
            reviewImageRepository.save(ReviewImageEntity(reviewEntity, it))
        }
        reviewRepository.save(reviewEntity)
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
    override fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val itemEntity = itemRepository.findByIdOrNull(postShoppingCartRequest.id)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")
        // 이미 장바구니에 해당 상품이 존재하는 경우
        userEntity.cartItems.find { it.item.id == postShoppingCartRequest.id && it.optionName == postShoppingCartRequest.option }
            ?.let { throw CustomHttp409("이미 장바구니에 있는 상품입니다.") }

        userEntity.cartItems.add(
            CartItemEntity(
                userEntity,
                itemEntity,
                postShoppingCartRequest.option,
                postShoppingCartRequest.quantity
            )
        )
    }

    @Transactional
    override fun patchShoppingCart(username: String, patchShoppingCartRequest: PatchShoppingCartRequest) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItemEntity = userEntity.cartItems.find { it.id == patchShoppingCartRequest.id }
            ?: throw CustomHttp404("장바구니에 해당 상품이 없습니다.")
        cartItemEntity.quantity = patchShoppingCartRequest.quantity
    }

    @Transactional
    override fun deleteShoppingCart(username: String, cartItemId: Long) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItemEntity = userEntity.cartItems.find { it.id == cartItemId }
            ?: throw CustomHttp404("장바구니에 해당 상품이 없습니다.")
        userEntity.cartItems.remove(cartItemEntity)
    }

    @Transactional
    override fun getRecentlyViewed(username: String): RecentItemsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val recentItemEntities = recentItemRepository.findAllByUser(userEntity)
        return RecentItemsResponse(recentItemEntities.map { recentItemEntity -> RecentItem.of(recentItemEntity) })
    }
}
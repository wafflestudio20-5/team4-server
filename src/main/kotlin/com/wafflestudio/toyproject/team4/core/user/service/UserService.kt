package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp403
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.common.CustomHttp409
import com.wafflestudio.toyproject.team4.core.board.api.response.InquiriesResponse
import com.wafflestudio.toyproject.team4.core.board.api.response.ReviewsResponse
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity.Color
import com.wafflestudio.toyproject.team4.core.board.database.InquiryRepository
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity
import com.wafflestudio.toyproject.team4.core.board.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity.Size
import com.wafflestudio.toyproject.team4.core.board.domain.Inquiry
import com.wafflestudio.toyproject.team4.core.board.domain.Review
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.style.database.FollowRepository
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.*
import com.wafflestudio.toyproject.team4.core.user.database.CartItemEntity
import com.wafflestudio.toyproject.team4.core.user.database.CartItemRepository
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseEntity
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseRepository
import com.wafflestudio.toyproject.team4.core.user.database.RecentItemRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.domain.CartItem
import com.wafflestudio.toyproject.team4.core.user.domain.Purchase
import com.wafflestudio.toyproject.team4.core.user.domain.RecentItem
import com.wafflestudio.toyproject.team4.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.math.ceil

interface UserService {
    fun getMe(username: String): UserMeResponse
    fun patchMe(username: String, patchMeRequest: PatchMeRequest)
    fun getUser(username: String?, userId: Long): UserResponse
    fun getUserStyles(userId: Long): StylesResponse
    fun getReviews(username: String): ReviewsResponse
    fun postReview(username: String, request: ReviewRequest)
    fun putReview(username: String, request: ReviewRequest)
    fun deleteReview(username: String, reviewId: Long)
    fun getPurchases(username: String): PurchaseItemsResponse
    fun postPurchases(username: String, request: PurchasesRequest)
    fun getShoppingCart(username: String): CartItemsResponse
    fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest)
    fun patchShoppingCart(username: String, patchShoppingCartRequest: PatchShoppingCartRequest)
    fun deleteShoppingCart(username: String, cartItemId: Long)
    fun getRecentlyViewed(username: String): RecentItemsResponse
    fun postRecentlyViewed(username: String, itemId: Long)

    fun getItemInquiries(username: String, index: Long, count: Long): InquiriesResponse
    fun putItemInquiries(username: String, putItemInquiriesRequest: PutItemInquiriesRequest)
    fun deleteItemInquiry(username: String, itemInquiryId: Long)
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val purchaseRepository: PurchaseRepository,
    private val cartItemRepository: CartItemRepository,
    private val recentItemRepository: RecentItemRepository,
    private val itemRepository: ItemRepository,
    private val inquiryRepository: InquiryRepository,
    private val followRepository: FollowRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    @Transactional
    override fun getMe(username: String): UserMeResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        return UserMeResponse(User.of(userEntity))
    }

    @Transactional
    override fun patchMe(username: String, patchMeRequest: PatchMeRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val newEncodedPassword = patchMeRequest.password?.let { passwordEncoder.encode(it) }
        user.update(patchMeRequest, newEncodedPassword)
    }

    @Transactional
    override fun getUser(username: String?, userId: Long): UserResponse {
        val followerUser = username?.let { userRepository.findByUsername(it) }
        val followingUser = userRepository.findByIdOrNull(userId)
            ?: throw CustomHttp404("존재하지 않는 사용자입니다.")
        val isFollow = followerUser?.let { followRepository.findRelation(followingUser.id, it.id) } ?: false
        val styleCount: Long = 1
        val followerCount: Long = 1
        val followingCount: Long = 1
        return UserResponse(
            user = User.of(followingUser),
            count = Count(styleCount, followerCount, followingCount),
            isFollow = isFollow,
        )
    }

    @Transactional
    override fun getUserStyles(userId: Long): StylesResponse {
        val userEntity = userRepository.findByIdOrNull(userId)
            ?: throw CustomHttp404("존재하지 않는 사용자입니다.")
        return StylesResponse(userEntity.styles.map { styleEntity -> StylePreview.of(styleEntity) })
    }

    @Transactional
    override fun getReviews(username: String): ReviewsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        return ReviewsResponse(userEntity.reviews.map { reviewEntity -> Review.of(reviewEntity) })
    }

    @Transactional
    override fun postReview(username: String, request: ReviewRequest) {
        val purchaseEntity = purchaseRepository.findByIdOrNull(request.id)
            ?: throw CustomHttp404("구매한 상품이 올바르지 않습니다.")
        val reviewEntity = ReviewEntity(
            user = purchaseEntity.user,
            purchase = purchaseEntity,
            rating = request.rating,
            content = request.content,
            image1 = request.images.getOrNull(0),
            image2 = request.images.getOrNull(1),
            image3 = request.images.getOrNull(2),
            size = Size.valueOf(request.size.uppercase()),
            color = Color.valueOf(request.color.uppercase()),
        )
        reviewRepository.save(reviewEntity)
        purchaseEntity.item.reviewCount++
    }

    @Transactional
    override fun putReview(username: String, request: ReviewRequest) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val reviewEntity = userEntity.reviews.find { it.id == request.id }
            ?: throw CustomHttp404("작성한 구매후기가 없습니다.")
        reviewEntity.update(request)
        reviewRepository.save(reviewEntity)
    }

    @Transactional
    override fun deleteReview(username: String, reviewId: Long) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val reviewEntity = userEntity.reviews.find { it.id == reviewId }
            ?: throw CustomHttp404("작성한 구매후기가 없습니다.")
        reviewEntity.purchase.item.reviewCount--
        userEntity.reviews.remove(reviewEntity)
    }

    @Transactional
    override fun getPurchases(username: String): PurchaseItemsResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val purchaseEntities = purchaseRepository.findAllByUser(userEntity)
        return PurchaseItemsResponse(
            purchaseEntities.map { purchaseEntity -> Purchase.of(purchaseEntity) }
        )
    }

    /* **********************************************************
    //                      Shopping Cart                      //
    ********************************************************** */

    @Transactional
    override fun postPurchases(username: String, request: PurchasesRequest) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        request.purchaseItems.forEach {
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
    override fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val itemEntity = itemRepository.findByIdOrNull(postShoppingCartRequest.id)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")
        // 이미 장바구니에 해당 상품이 존재하는 경우
        userEntity.cartItems.find {
            it.item.id == postShoppingCartRequest.id &&
                it.optionName == postShoppingCartRequest.option
        }?.let { throw CustomHttp409("이미 장바구니에 있는 상품입니다.") }

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

    /* **********************************************************
    //                    Recently Viewed                      //
    ********************************************************** */

    @Transactional
    override fun getRecentlyViewed(username: String): RecentItemsResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val recentItemList = recentItemRepository
            .findAllByUserOrderByViewedDateTimeDesc(user)
            .groupBy({ it.item }, { it })
            .map { entry -> entry.value.maxBy { it.id } }
            .filterIndexed { idx, _ -> idx < 12 }

        return RecentItemsResponse(
            recentItems = recentItemList.map { recentItem -> RecentItem.of(recentItem) }
        )
    }

    @Transactional
    override fun postRecentlyViewed(username: String, itemId: Long) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")

        user.viewItem(item)
    }

    /* **********************************************************
    //                     Item Inquiries                      //
    ********************************************************** */

    @Transactional
    override fun getItemInquiries(username: String, index: Long, count: Long): InquiriesResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val itemInquiryList = inquiryRepository.findAllByUserOrderByCreatedDateTimeDesc(user, index, count)
        return InquiriesResponse(
            inquiries = itemInquiryList.map { inquiry -> Inquiry.of(inquiry) },
            totalPages = ceil(user.itemInquiries.size.toDouble() / count).toLong()
        )
    }

    @Transactional
    override fun putItemInquiries(username: String, putItemInquiriesRequest: PutItemInquiriesRequest) {
        val targetInquiryId = putItemInquiriesRequest.id
        val targetItemInquiry = inquiryRepository.findByIdOrNull(targetInquiryId)
            ?: throw CustomHttp404("작성한 상품 문의가 없습니다.")

        if (targetItemInquiry.user.username != username)
            throw CustomHttp403("수정 권한이 없습니다.")

        targetItemInquiry.update(putItemInquiriesRequest)
    }

    @Transactional
    override fun deleteItemInquiry(username: String, itemInquiryId: Long) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val itemInquiry = user.itemInquiries.find { it.id == itemInquiryId }
            ?: throw CustomHttp404("작성한 상품 문의가 없습니다.")
        user.itemInquiries.remove(itemInquiry)
    }
}

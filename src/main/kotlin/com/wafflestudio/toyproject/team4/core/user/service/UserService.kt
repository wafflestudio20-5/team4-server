package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp403
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.common.CustomHttp409
import com.wafflestudio.toyproject.team4.core.board.api.response.InquiriesResponse
import com.wafflestudio.toyproject.team4.core.board.api.response.ReviewsResponse
import com.wafflestudio.toyproject.team4.core.board.database.InquiryRepository
import com.wafflestudio.toyproject.team4.core.board.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.board.domain.Inquiry
import com.wafflestudio.toyproject.team4.core.board.domain.Review
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.style.database.FollowEntity
import com.wafflestudio.toyproject.team4.core.style.database.FollowRepository
import com.wafflestudio.toyproject.team4.core.style.domain.Style
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.*
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseRepository
import com.wafflestudio.toyproject.team4.core.user.database.RecentItemRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
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
    fun getFollowers(userId: Long): FollowersResponse
    fun getFollowings(userId: Long): FollowingsResponse
    fun getIsFollow(currentUser: UserEntity?, closetOwner: UserEntity): Boolean
    fun getUserStyles(userId: Long): StylesResponse
    fun follow(username: String, userId: Long)
    fun unfollow(username: String, userId: Long)

    fun searchUsers(query: String?, index: Long, count: Long): UserSearchResponse
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
    private val recentItemRepository: RecentItemRepository,
    private val itemRepository: ItemRepository,
    private val inquiryRepository: InquiryRepository,
    private val followRepository: FollowRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    @Transactional
    override fun getMe(username: String): UserMeResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return UserMeResponse(User.of(user))
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
        val currentUser = username?.let { userRepository.findByUsernameOrNullWithFollows(it) }
        val closetOwner = userRepository.findByIdOrNullWithFollows(userId)
            ?: throw CustomHttp404("존재하지 않는 사용자입니다.")

        val styleCount: Long = closetOwner.styles.size.toLong()
        val followerCount: Long = closetOwner.followers.count { it.isActive }.toLong()
        val followingCount: Long = closetOwner.followings.count { it.isActive }.toLong()
        val isFollow = getIsFollow(currentUser, closetOwner)

        return UserResponse(
            user = User.of(closetOwner),
            count = Count(styleCount, followerCount, followingCount),
            isFollow = isFollow,
        )
    }

    override fun getIsFollow(currentUser: UserEntity?, closetOwner: UserEntity): Boolean {
        val relationHistory = currentUser?.followings?.find { it.followed == closetOwner }

        return relationHistory?.isActive ?: false
    }

    override fun getFollowers(userId: Long): FollowersResponse {
        val closetOwner = userRepository.findByIdOrNullWithFollowersWithUsers(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followers = closetOwner.followers.map { User.simplify(it.following) }

        return FollowersResponse(followers)
    }

    override fun getFollowings(userId: Long): FollowingsResponse {
        val closetOwner = userRepository.findByIdOrNullWithFollowingsWithUsers(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followings = closetOwner.followings.map { User.simplify(it.followed) }

        return FollowingsResponse(followings)
    }

    @Transactional
    override fun getUserStyles(userId: Long): StylesResponse {
        val user = userRepository.findByIdOrNullWithStylesOrderByRecentDesc(userId)
            ?: throw CustomHttp404("존재하지 않는 사용자입니다.")

        return StylesResponse(user.styles.map { Style.preview(it) })
    }

    @Transactional
    override fun follow(username: String, userId: Long) {
        val followingUser = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followedUser = userRepository.findByIdOrNull(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val relation = followRepository.findRelation(followingUser, followedUser)
        if (relation == null) followRepository.save(FollowEntity(followingUser, followedUser))
        else relation.activate()
    }

    @Transactional
    override fun unfollow(username: String, userId: Long) {
        val followingUser = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followedUser = userRepository.findByIdOrNull(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val follow = followRepository.findRelation(followingUser, followedUser)
            ?: throw CustomHttp404("해당 사용자를 팔로우하고 있지 않습니다.")

        follow.deactivate()
    }

    override fun searchUsers(query: String?, index: Long, count: Long): UserSearchResponse {
        if (query == "" || query == null) throw CustomHttp400("검색어를 입력하세요.")
        val users = userRepository.searchByQuery(query, index, count)

        return UserSearchResponse(users.map { User.simplify(it) })
    }

    /* **********************************************************
    //                         Reviews                         //
    ********************************************************** */

    @Transactional
    override fun getReviews(username: String): ReviewsResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return ReviewsResponse(
            reviews = user.reviews.map { entity -> Review.of(entity) }
        )
    }

    @Transactional
    override fun postReview(username: String, request: ReviewRequest) {
        val purchaseItem = purchaseRepository.findByIdOrNull(request.id)
            ?: throw CustomHttp404("구매한 상품이 올바르지 않습니다.")

        purchaseItem.writeReview(request)
    }

    @Transactional
    override fun putReview(username: String, request: ReviewRequest) {
        val review = reviewRepository.findByIdOrNull(request.id)
            ?: throw CustomHttp404("존재하지 않는 구매후기입니다.")
        if (review.user.username != username)
            throw CustomHttp403("사용자의 구매후기가 아닙니다.")

        review.update(request)
    }

    @Transactional
    override fun deleteReview(username: String, reviewId: Long) {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw CustomHttp404("존재하지 않는 구매후기입니다.")
        if (review.user.username != username)
            throw CustomHttp403("사용자의 구매후기가 아닙니다.")

        reviewRepository.delete(review)
    }

    /* **********************************************************
    //                        Purchases                       //
    ********************************************************** */

    @Transactional
    override fun getPurchases(username: String): PurchaseItemsResponse {
        val user = userRepository.findByUsernameFetchJoinPurchases(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return PurchaseItemsResponse(
            purchaseItems = user.purchases.map { entity -> Purchase.of(entity) }
        )
    }

    @Transactional
    override fun postPurchases(username: String, request: PurchasesRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        request.purchaseItems.forEach { info ->
            val orderItem = itemRepository.findByIdOrNull(info.id)
                ?: throw CustomHttp404("아이템 정보가 올바르지 않습니다.")

            user.purchase(orderItem, info)
        }
    }

    /* **********************************************************
   //                      Shopping Cart                      //
   ********************************************************** */

    @Transactional
    override fun getShoppingCart(username: String): CartItemsResponse {
        val user = userRepository.findByUsernameFetchJoinCartItems(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return CartItemsResponse(
            cartItems = user.cartItems.map { entity -> CartItem.of(entity) }
        )
    }

    @Transactional
    override fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val orderItem = itemRepository.findByIdOrNull(postShoppingCartRequest.id)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")

        // 이미 장바구니에 해당 상품이 존재하는 경우
        val existingCartItem = user.cartItems.find {
            it.item.id == postShoppingCartRequest.id &&
                it.optionName == postShoppingCartRequest.option
        } ?: throw CustomHttp409("이미 장바구니에 있는 상품입니다.")

        user.addToCart(orderItem, postShoppingCartRequest)
    }

    @Transactional
    override fun patchShoppingCart(username: String, patchShoppingCartRequest: PatchShoppingCartRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItem = user.cartItems.find { it.id == patchShoppingCartRequest.id }
            ?: throw CustomHttp404("장바구니에 해당 상품이 없습니다.")

        cartItem.updateQuantity(patchShoppingCartRequest.quantity)
    }

    @Transactional
    override fun deleteShoppingCart(username: String, cartItemId: Long) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItem = user.cartItems.find { it.id == cartItemId }
            ?: throw CustomHttp404("장바구니에 해당 상품이 없습니다.")

        user.cartItems.remove(cartItem)
    }

    /* **********************************************************
    //                    Recently Viewed                      //
    ********************************************************** */

    @Transactional
    override fun getRecentlyViewed(username: String): RecentItemsResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val recentItems = recentItemRepository
            .findAllByUserOrderByViewedDateTimeDesc(user)
            .groupBy({ it.item }, { it })
            .map { entry -> entry.value.maxBy { it.id } }
            .filterIndexed { idx, _ -> idx < 12 }

        return RecentItemsResponse(
            recentItems = recentItems.map { entity -> RecentItem.of(entity) }
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

        val itemInquiries = inquiryRepository.findAllByUserOrderByCreatedDateTimeDesc(user, index, count)
        return InquiriesResponse(
            inquiries = itemInquiries.map { entity -> Inquiry.of(entity) },
            totalPages = ceil(user.itemInquiries.size.toDouble() / count).toLong()
        )
    }

    @Transactional
    override fun putItemInquiries(username: String, putItemInquiriesRequest: PutItemInquiriesRequest) {
        val targetItemInquiry = inquiryRepository.findByIdOrNull(putItemInquiriesRequest.id)
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

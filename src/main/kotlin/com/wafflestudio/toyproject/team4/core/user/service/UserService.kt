package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.user.api.response.PurchaseResponse
import com.wafflestudio.toyproject.team4.core.user.api.response.ReviewResponse
import com.wafflestudio.toyproject.team4.core.user.api.response.UserResponse
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseRepository
import com.wafflestudio.toyproject.team4.core.user.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface UserService {
    fun getMe(username: String): UserResponse
    fun getReviews(username: String): MutableList<ReviewResponse>
    fun getPurchases(username: String): MutableList<PurchaseResponse>
//    fun getShoppingCart(username: String): MutableList<Item>
//    fun getRecentlyViewed(username: String): MutableList<Item>
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val purchaseRepository: PurchaseRepository,
//    private val itemRepository: ItemRepository,
) : UserService {

    @Transactional
    override fun getMe(username: String): UserResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val userResponse = UserResponse.of(userEntity)
        userResponse.reviews = reviewRepository.getReviewResponses(userEntity.reviewEntities)
        return userResponse
    }

    @Transactional
    override fun getReviews(username: String): MutableList<ReviewResponse> {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        return reviewRepository.getReviewResponses(userEntity.reviewEntities)
    }

    @Transactional
    override fun getPurchases(username: String): MutableList<PurchaseResponse> {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        return purchaseRepository.getPurchaseResponses(userEntity.purchaseEntities)
    }
//
//    @Transactional
//    override fun getShoppingCart(username: String): MutableList<Item> {
//        val userEntity = userRepository.findByUsername(username)
//            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
//        return itemRepository.getItems(userEntity.shoppingCart)
//    }
//
//    @Transactional
//    override fun getRecentlyViewed(username: String): MutableList<Item> {
//        val userEntity = userRepository.findByUsername(username)
//            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
//        return itemRepository.getItems(userEntity.recentlyViewed)
//    }
}
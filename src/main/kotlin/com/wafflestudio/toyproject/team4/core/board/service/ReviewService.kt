package com.wafflestudio.toyproject.team4.core.board.service

import com.wafflestudio.toyproject.team4.common.CustomHttp403
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.board.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.board.api.response.ReviewsResponse
import com.wafflestudio.toyproject.team4.core.board.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.board.domain.Review
import com.wafflestudio.toyproject.team4.core.purchase.database.PurchaseRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface ReviewService {
    fun getReviews(username: String): ReviewsResponse
    fun postReview(username: String, request: ReviewRequest)
    fun putReview(username: String, request: ReviewRequest)
    fun deleteReview(username: String, reviewId: Long)
}

@Service
class ReviewServiceImpl(
    private val userRepository: UserRepository,
    private val purchaseRepository: PurchaseRepository,
    private val reviewRepository: ReviewRepository
) : ReviewService {

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

        review.purchase.deleteReview(review.rating)
        reviewRepository.delete(review)
    }
}

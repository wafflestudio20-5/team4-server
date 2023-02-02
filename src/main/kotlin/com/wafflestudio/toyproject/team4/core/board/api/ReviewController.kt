package com.wafflestudio.toyproject.team4.core.board.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.board.service.ReviewService
import com.wafflestudio.toyproject.team4.core.board.api.request.ReviewRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class ReviewController(
    private val reviewService: ReviewService
) {
    @Authenticated
    @GetMapping("/me/reviews")
    fun getReviews(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(reviewService.getReviews(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/review")
    fun postReview(
        @RequestBody reviewRequest: ReviewRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(reviewService.postReview(username, reviewRequest), HttpStatus.CREATED)

    @Authenticated
    @PutMapping("/me/review")
    fun putReview(
        @RequestBody reviewRequest: ReviewRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(reviewService.putReview(username, reviewRequest), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/me/review/{reviewId}")
    fun deleteReview(
        @PathVariable(value = "reviewId") reviewId: Long,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(reviewService.deleteReview(username, reviewId), HttpStatus.OK)
}
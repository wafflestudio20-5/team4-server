package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RecentlyViewedRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.UserResponse
import com.wafflestudio.toyproject.team4.core.user.service.AuthTokenService
import com.wafflestudio.toyproject.team4.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val authTokenService: AuthTokenService,
    private val userService: UserService
) {
    @Authenticated
    @GetMapping("/me")
    fun getMe(
        @UserContext username: String,
    ) = ResponseEntity(userService.getMe(username), HttpStatus.OK)

    @GetMapping("/{userId}/styles")
    fun getUserStyles(
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(userService.getUserStyles(userId), HttpStatus.OK)

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable(value = "userId") userId: Long,
        @RequestHeader(value = "Authorization") authToken: String?,
    ): UserResponse {
        val username = authToken?.let { authTokenService.getUsernameFromToken(it) }
        return userService.getUser(username, userId)
    }

    @GetMapping("/{userId}/followers")
    fun getFollowers(
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(userService.getFollowers(userId), HttpStatus.OK)

    @GetMapping("/{userId}/followings")
    fun getFollowings(
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(userService.getFollowings(userId), HttpStatus.OK)

    @Authenticated
    @PostMapping("/{userId}/follow")
    fun follow(
        @UserContext username: String,
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(userService.follow(username, userId), HttpStatus.CREATED)

    @Authenticated
    @DeleteMapping("/{userId}/follow")
    fun unfollow(
        @UserContext username: String,
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(userService.unfollow(username, userId), HttpStatus.OK)

    @Authenticated
    @PatchMapping("/me")
    fun patchMe(
        @UserContext username: String,
        @RequestBody patchMeRequest: PatchMeRequest
    ) = ResponseEntity(userService.patchMe(username, patchMeRequest), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/reviews")
    fun getReviews(
        @UserContext username: String,
    ) = ResponseEntity(userService.getReviews(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/review")
    fun postReview(
        @RequestBody request: ReviewRequest,
        @UserContext username: String,
    ) = ResponseEntity(userService.postReview(username, request), HttpStatus.CREATED)

    @Authenticated
    @PutMapping("/me/review")
    fun putReview(
        @RequestBody request: ReviewRequest,
        @UserContext username: String,
    ) = ResponseEntity(userService.putReview(username, request), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/me/review/{reviewId}")
    fun deleteReview(
        @PathVariable(value = "reviewId") reviewId: Long,
        @UserContext username: String,
    ) = ResponseEntity(userService.deleteReview(username, reviewId), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/comment")
    fun postComment(
        @RequestBody request: PostCommentRequest,
        @UserContext username: String,
    ) = ResponseEntity(userService.postComment(username, request), HttpStatus.CREATED)

    @Authenticated
    @PutMapping("/me/comment/{commentId}")
    fun putComment(
        @PathVariable(value = "commentId") commentId: Long,
        @RequestBody request: PutCommentRequest,
        @UserContext username: String,
    ) = ResponseEntity(userService.putComment(username, request, commentId), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/me/comment/{commentId}")
    fun deleteComment(
        @PathVariable(value = "commentId") commentId: Long,
        @UserContext username: String,
    ) = ResponseEntity(userService.deleteComment(username, commentId), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/purchases")
    fun getPurchases(
        @UserContext username: String,
    ) = ResponseEntity(userService.getPurchases(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/purchases")
    fun postPurchases(
        @RequestBody request: PurchasesRequest,
        @UserContext username: String,
    ) {
        userService.postPurchases(username, request)
    }

    @Authenticated
    @GetMapping("/me/shopping-cart")
    fun getShoppingCart(
        @UserContext username: String,
    ) = ResponseEntity(userService.getShoppingCart(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/shopping-cart")
    fun postShoppingCart(
        @UserContext username: String,
        @RequestBody postShoppingCartRequest: PostShoppingCartRequest
    ) = ResponseEntity(userService.postShoppingCart(username, postShoppingCartRequest), HttpStatus.CREATED)

    @Authenticated
    @PatchMapping("/me/shopping-cart")
    fun patchShoppingCart(
        @UserContext username: String,
        @RequestBody patchShoppingCartRequest: PatchShoppingCartRequest
    ) = ResponseEntity(userService.patchShoppingCart(username, patchShoppingCartRequest), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/me/shopping-cart/{id}")
    fun deleteShoppingCart(
        @UserContext username: String,
        @PathVariable(value = "id") cartItemId: Long
    ) = ResponseEntity(userService.deleteShoppingCart(username, cartItemId), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/recently-viewed")
    fun getRecentlyViewed(
        @UserContext username: String,
    ) = ResponseEntity(userService.getRecentlyViewed(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/recently-viewed")
    fun postRecentlyViewed(
        @UserContext username: String,
        @RequestBody postRecentlyViewedRequest: RecentlyViewedRequest
    ) = ResponseEntity(
        userService.postRecentlyViewed(username, postRecentlyViewedRequest.itemId),
        HttpStatus.CREATED
    )

    @Authenticated
    @GetMapping("/me/item-inquiries")
    fun getItemInquiries(
        @UserContext username: String,
        @RequestParam index: Long?,
        @RequestParam count: Long?
    ) = userService.getItemInquiries(username, index ?: 0L, count ?: 5L)

    @Authenticated
    @PutMapping("/me/item-inquiries")
    fun putItemInquiries(
        @UserContext username: String,
        @RequestBody putItemInquiriesRequest: PutItemInquiriesRequest
    ) = ResponseEntity(
        userService.putItemInquiries(username, putItemInquiriesRequest),
        HttpStatus.OK
    )

    @Authenticated
    @DeleteMapping("/me/item-inquiry/{id}")
    fun deleteItemInquiry(
        @UserContext username: String,
        @PathVariable(value = "id") itemInquiryId: Long
    ) = ResponseEntity(
        userService.deleteItemInquiry(username, itemInquiryId),
        HttpStatus.OK
    )

    @GetMapping("/search")
    fun searchUsers(
        @RequestParam query: String?,
        @RequestParam index: Long?,
        @RequestParam count: Long?,
    ) = ResponseEntity(
        userService.searchUsers(query, index ?: 0L, count ?: 10L),
        HttpStatus.OK
    )
}

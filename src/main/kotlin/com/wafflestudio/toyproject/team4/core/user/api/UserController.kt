package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.user.api.request.DeleteReviewRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RecentlyViewedRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @Authenticated
    @GetMapping("/me")
    fun getMe(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getMe(username), HttpStatus.OK)

    @GetMapping("/{userId}/styles")
    fun getUserStyles(
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(userService.getUserStyles(userId), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/reviews")
    fun getReviews(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getReviews(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/reviews")
    fun postReview(
        @RequestBody request: ReviewRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) {
        userService.postReview(username, request)
    }

    @Authenticated
    @PutMapping("/me/reviews")
    fun putReview(
        @RequestBody request: ReviewRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) {
        userService.putReview(username, request)
    }

    @Authenticated
    @DeleteMapping("/me/reviews")
    fun deleteReview(
        @RequestBody request: DeleteReviewRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) {
        userService.deleteReview(username, request)
    }

    @Authenticated
    @GetMapping("/me/purchases")
    fun getPurchases(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getPurchases(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/purchases")
    fun postPurchases(
        @RequestBody request: PurchasesRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) {
        userService.postPurchases(username, request)
    }

    @Authenticated
    @GetMapping("/me/shopping-cart")
    fun getShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getShoppingCart(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/shopping-cart")
    fun postShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody postShoppingCartRequest: PostShoppingCartRequest
    ) = ResponseEntity(userService.postShoppingCart(username, postShoppingCartRequest), HttpStatus.CREATED)

    @Authenticated
    @PatchMapping("/me/shopping-cart")
    fun patchShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody patchShoppingCartRequest: PatchShoppingCartRequest
    ) = ResponseEntity(userService.patchShoppingCart(username, patchShoppingCartRequest), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/me/shopping-cart/{id}")
    fun deleteShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @PathVariable(value = "id") cartItemId: Long
    ) = ResponseEntity(userService.deleteShoppingCart(username, cartItemId), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/recently-viewed")
    fun getRecentlyViewed(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getRecentlyViewed(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/me/recently-viewed")
    fun postRecentlyViewed(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody postRecentlyViewedRequest: RecentlyViewedRequest
    ) = ResponseEntity(
        userService.postRecentlyViewed(username, postRecentlyViewedRequest.itemId),
        HttpStatus.CREATED
    )

    @Authenticated
    @GetMapping("/me/item-inquiries")
    fun getItemInquiries(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = userService.getItemInquiries(username)

    @Authenticated
    @PutMapping("/me/item-inquiries")
    fun putItemInquiries(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody putItemInquiriesRequest: PutItemInquiriesRequest
    ) = ResponseEntity(
        userService.putItemInquiries(username, putItemInquiriesRequest),
        HttpStatus.OK
    )

    @Authenticated
    @DeleteMapping("/me/item-inquiry/{id}")
    fun deleteItemInquiry(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @PathVariable(value = "id") itemInquiryId: Long
    ) = ResponseEntity(
        userService.deleteItemInquiry(username, itemInquiryId),
        HttpStatus.OK
    )
}

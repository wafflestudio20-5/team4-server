package com.wafflestudio.toyproject.team4.core.user.api


import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.user.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.PutShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


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

    @Authenticated
    @GetMapping("/me/reviews")
    fun getReviews(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getReviews(username), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/purchases")
    fun getPurchases(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getPurchases(username), HttpStatus.OK)

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
    @PutMapping("/me/shopping-cart")
    fun putShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody putShoppingCartRequest: PutShoppingCartRequest
    ) = ResponseEntity(userService.putShoppingCart(username, putShoppingCartRequest), HttpStatus.OK)

    @Authenticated
    @GetMapping("/me/recently-viewed")
    fun getRecentlyViewed(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getRecentlyViewed(username), HttpStatus.OK)
}

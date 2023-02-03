package com.wafflestudio.toyproject.team4.core.user.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.RecentlyViewedRequest
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
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(userService.getMe(username), HttpStatus.OK)

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
        @RequestParam index: Long?,
        @RequestParam count: Long?
    ) = userService.getItemInquiries(username, index ?: 0L, count ?: 5L)

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

package com.wafflestudio.toyproject.team4.core.style.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.style.api.request.PatchStyleRequest
import com.wafflestudio.toyproject.team4.core.style.api.request.PostStyleRequest
import com.wafflestudio.toyproject.team4.core.style.api.response.StyleResponse
import com.wafflestudio.toyproject.team4.core.style.service.StyleService
import com.wafflestudio.toyproject.team4.core.user.service.AuthTokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class StyleController(
    private val authTokenService: AuthTokenService,
    private val styleService: StyleService
) {

    @GetMapping("/styles")
    fun getStyles(
        @RequestParam index: Long?,
        @RequestParam count: Long?,
        @RequestParam sort: String?
    ) = styleService.getStyles(index ?: 0L, count ?: 4L, sort)

    @GetMapping("/style/{styleId}")
    fun getStyle(
        @RequestHeader(value = "Authorization") authToken: String?,
        @PathVariable(value = "styleId") styleId: Long
    ): StyleResponse {
        val username = authToken?.let { authTokenService.getUsernameFromToken(it) }
        return styleService.getStyle(username, styleId)
    }

    @GetMapping("/user/{userId}/styles")
    fun getUserStyles(
        @PathVariable(value = "userId") userId: Long
    ) = ResponseEntity(styleService.getUserStyles(userId), HttpStatus.OK)

    @Authenticated
    @PostMapping("/style")
    fun postStyle(
        @UserContext username: String,
        @RequestBody postStyleRequest: PostStyleRequest
    ) = ResponseEntity(
        styleService.postStyle(username, postStyleRequest),
        HttpStatus.CREATED
    )

    @Authenticated
    @PatchMapping("/style/{styleId}")
    fun patchStyle(
        @UserContext username: String,
        @PathVariable(value = "styleId") styleId: Long,
        @RequestBody patchStyleRequest: PatchStyleRequest
    ) = ResponseEntity(
        styleService.patchStyle(username, styleId, patchStyleRequest),
        HttpStatus.OK
    )

    @Authenticated
    @DeleteMapping("/style/{styleId}")
    fun deleteStyle(
        @UserContext username: String,
        @PathVariable(value = "styleId") styleId: Long,
    ) = ResponseEntity(
        styleService.deleteStyle(username, styleId),
        HttpStatus.OK
    )

    @Authenticated
    @PostMapping("/style/{styleId}/like")
    fun postLike(
        @UserContext username: String,
        @PathVariable(value = "styleId") styleId: Long,
    ) = ResponseEntity(
        styleService.postLike(username, styleId),
        HttpStatus.CREATED
    )

    @Authenticated
    @DeleteMapping("/style/{styleId}/like")
    fun deleteLike(
        @UserContext username: String,
        @PathVariable(value = "styleId") styleId: Long,
    ) = ResponseEntity(
        styleService.deleteLike(username, styleId),
        HttpStatus.OK
    )
}

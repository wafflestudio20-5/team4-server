package com.wafflestudio.toyproject.team4.core.style.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.style.service.StyleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
    private val styleService: StyleService
) {

    @GetMapping("/styles")
    fun getStyles(
        @RequestParam index: Long?,
        @RequestParam count: Long?,
        @RequestParam sort: String?
    ) = styleService.getStyles(index ?: 0L, count ?: 4L, sort)

    @Authenticated
    @GetMapping("/style/{styleId}")
    fun getService(
        @RequestHeader(value = "Authorization") authorization: String?,
        @UserContext username: String?,
        @PathVariable(value = "styleId") styleId: Long
    ) = styleService.getStyle(username, styleId)

    @Authenticated
    @PostMapping("/style")
    fun postItemInquiry(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody postStyleRequest: PostStyleRequest
    ) = ResponseEntity(
        styleService.postStyle(username, postStyleRequest),
        HttpStatus.CREATED
    )
}
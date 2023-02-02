package com.wafflestudio.toyproject.team4.core.board.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.board.api.request.PostItemInquiryRequest
import com.wafflestudio.toyproject.team4.core.board.service.InquiryService
import com.wafflestudio.toyproject.team4.core.board.api.request.PutItemInquiriesRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class InquiryController(
    private val inquiryService: InquiryService
) {
    @Authenticated
    @PostMapping("/item/{id}/inquiry")
    fun postItemInquiry(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @PathVariable(value = "id") itemId: Long,
        @RequestBody postItemInquiryRequest: PostItemInquiryRequest
    ) = ResponseEntity(
        inquiryService.postItemInquiry(username, itemId, postItemInquiryRequest),
        HttpStatus.CREATED
    )


    @Authenticated
    @PutMapping("/user/me/item-inquiries")
    fun putItemInquiries(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody putItemInquiriesRequest: PutItemInquiriesRequest
    ) = ResponseEntity(
        inquiryService.putItemInquiries(username, putItemInquiriesRequest),
        HttpStatus.OK
    )

    @Authenticated
    @DeleteMapping("/user/me/item-inquiry/{id}")
    fun deleteItemInquiry(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @PathVariable(value = "id") itemInquiryId: Long
    ) = ResponseEntity(
        inquiryService.deleteItemInquiry(username, itemInquiryId),
        HttpStatus.OK
    )
}
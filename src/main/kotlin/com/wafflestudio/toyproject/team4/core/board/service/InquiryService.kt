package com.wafflestudio.toyproject.team4.core.board.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp403
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.board.api.request.PostItemInquiryRequest
import com.wafflestudio.toyproject.team4.core.board.database.InquiryRepository
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.board.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface InquiryService {
    fun postItemInquiry(username: String, itemId: Long, postItemInquiryRequest: PostItemInquiryRequest)
    fun putItemInquiries(username: String, putItemInquiriesRequest: PutItemInquiriesRequest)
    fun deleteItemInquiry(username: String, itemInquiryId: Long)
}

@Service
class InquiryServiceImpl(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
    private val inquiryRepository: InquiryRepository
) : InquiryService {

    @Transactional
    override fun postItemInquiry(username: String, itemId: Long, postItemInquiryRequest: PostItemInquiryRequest) {
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")
        val user = userRepository.findByUsername(username)!!

        // check whether the option that user selected is one of the option of the item
        val newOption = postItemInquiryRequest.option
        val itemOptions = item.options?.map { it.optionName } ?: listOf()
        if (!newOption.isNullOrEmpty() && !itemOptions.contains(newOption))
            throw CustomHttp400("상품에 존재하지 않는 옵션입니다.")

        // make a new ItemInquiry object
        item.writeInquiry(user, postItemInquiryRequest)
    }

    @Transactional
    override fun putItemInquiries(username: String, putItemInquiriesRequest: PutItemInquiriesRequest) {
        val targetItemInquiry = inquiryRepository.findByIdOrNull(putItemInquiriesRequest.id)
            ?: throw CustomHttp404("작성한 상품 문의가 없습니다.")
        if (targetItemInquiry.user.username != username)
            throw CustomHttp403("수정 권한이 없습니다.")

        targetItemInquiry.update(putItemInquiriesRequest)
    }

    @Transactional
    override fun deleteItemInquiry(username: String, itemInquiryId: Long) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val itemInquiry = user.itemInquiries.find { it.id == itemInquiryId }
            ?: throw CustomHttp404("작성한 상품 문의가 없습니다.")

        user.itemInquiries.remove(itemInquiry)
    }
}

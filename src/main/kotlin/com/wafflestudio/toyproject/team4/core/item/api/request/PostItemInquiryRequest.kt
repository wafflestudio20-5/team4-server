package com.wafflestudio.toyproject.team4.core.item.api.request

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity.Type
import com.wafflestudio.toyproject.team4.core.board.database.InquiryImageEntity
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity

data class PostItemInquiryRequest(
    val type: String,      // size | delivery | restock | detail
    val option: String?,
    val isSecret: Boolean,
    val title: String?,
    val content: String?,
    val images: List<String>? = mutableListOf()
) {
    init {
        if(content == null) throw CustomHttp400("문의 내용을 입력해주세요.")
        if(title == null) throw CustomHttp400("제목은 필수 입력값입니다.")
    }

    fun toEntity(user: UserEntity, item: ItemEntity): InquiryEntity {
        val newInquiry = InquiryEntity(
            user = user,
            item = item,
            title = title!!,
            content = content!!,
            type = Type.valueOf(type.uppercase()),
            optionName = option,
            isSecret = isSecret,
            isAnswered = false,
        )
        newInquiry.images = images!!.map {
            url -> InquiryImageEntity(newInquiry, url)
        }.toMutableList()
        return newInquiry
    }
}

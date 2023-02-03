package com.wafflestudio.toyproject.team4.core.board.api.request

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity
import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity.Type
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity

data class PostItemInquiryRequest(
    val type: String,
    val option: String?,
    val isSecret: Boolean,
    val title: String?,
    val content: String?,
    val images: List<String>?
) {
    init {
        if (content == null) throw CustomHttp400("문의 내용을 입력해주세요.")
        if (title == null) throw CustomHttp400("제목은 필수 입력값입니다.")
    }

    fun toEntity(user: UserEntity, item: ItemEntity) =
        InquiryEntity(
            user = user,
            item = item,
            title = title!!,
            content = content!!,
            type = Type.valueOf(type.uppercase()),
            optionName = option,
            image1 = images?.getOrNull(0),
            image2 = images?.getOrNull(1),
            image3 = images?.getOrNull(2),
            isSecret = isSecret,
            isAnswered = false,
        )
}

package com.wafflestudio.toyproject.team4.core.user.api.request

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.board.database.InquiryEntity

data class PutItemInquiriesRequest(
    val id: Long,
    val type: String,
    val title: String?,
    val content: String?,
    val option: String? = null,
    val isSecret: Boolean,
    val images: List<String>? = null
) {
    init {
        try {
            InquiryEntity.Type.valueOf(type.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomHttp400("유효하지 않은 문의 유형입니다.")
        }

        if (content.isNullOrEmpty()) {
            throw CustomHttp400("내용을 입력해주세요")
        } else if (title.isNullOrEmpty()) {
            throw CustomHttp400("반드시 제목을 입력해주세요.")
        }
    }
}

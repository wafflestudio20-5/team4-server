package com.wafflestudio.toyproject.team4.core.board.api.response

import com.wafflestudio.toyproject.team4.core.board.domain.ItemInquiry

data class ItemInquiriesResponse(
    val inquiries: List<ItemInquiry>
)

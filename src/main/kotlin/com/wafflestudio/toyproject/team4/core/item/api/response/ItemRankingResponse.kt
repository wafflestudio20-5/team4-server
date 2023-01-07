package com.wafflestudio.toyproject.team4.core.item.api.response

import com.wafflestudio.toyproject.team4.core.item.domain.Item

data class ItemRankingResponse (
    val items: List<Item>,
    val nextItemId: Long?
)
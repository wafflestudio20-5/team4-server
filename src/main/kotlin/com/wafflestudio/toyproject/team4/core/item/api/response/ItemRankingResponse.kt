package com.wafflestudio.toyproject.team4.core.item.api.response

import com.wafflestudio.toyproject.team4.core.item.domain.RankingItem

data class ItemRankingResponse(
    val items: List<RankingItem>,
    val totalPages: Long
)

package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.domain.RecentItem

data class RecentItemsResponse(
    val recentItems: List<RecentItem>
)

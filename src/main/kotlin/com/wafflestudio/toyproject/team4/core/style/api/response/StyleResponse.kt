package com.wafflestudio.toyproject.team4.core.style.api.response

import com.wafflestudio.toyproject.team4.core.style.domain.Style

data class StyleResponse(
    val style: Style,
    val likedCount: Long? = 5L,
    val isFollow: Boolean? = false,
    val isLike: Boolean? = false,
)

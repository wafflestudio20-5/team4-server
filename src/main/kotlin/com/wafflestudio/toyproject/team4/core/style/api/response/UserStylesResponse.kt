package com.wafflestudio.toyproject.team4.core.style.api.response

import com.wafflestudio.toyproject.team4.core.style.domain.Style

data class UserStylesResponse(
    val styles: List<Style.Preview>
)

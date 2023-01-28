package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity

data class StylesResponse(
    val styles: List<Style>
)

data class Style(
    val id: Long,
    val image: String,
) {
    companion object {
        fun of(entity: StyleEntity): Style = entity.run {
            Style(
                id = id,
                image = image1,
            )
        }
    }
}

package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity

data class StylesResponse(
    val styles: List<StylePreview>
)

data class StylePreview(
    val id: Long,
    val image: String,
) {
    companion object {
        fun of(entity: StyleEntity): StylePreview = entity.run {
            StylePreview(
                id = id,
                image = image1,
            )
        }
    }
}

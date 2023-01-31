package com.wafflestudio.toyproject.team4.core.style.api.request

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity

data class PostStyleRequest(
    val images: List<String>?,
    val itemIds: List<Long>?,
    val content: String?,
    val hashtag: String?
) {
    init {
        if (images.isNullOrEmpty()) throw CustomHttp400("스타일 사진을 업로드해주세요.")
        if (itemIds.isNullOrEmpty()) throw CustomHttp400("스타일에 사용된 상품을 등록해주세요.")
    }

    fun toEntity(
        user: UserEntity
    ): StyleEntity {
        val newStyle = StyleEntity(
            user = user,
            image1 = images!!.first(),
            image2 = images.getOrNull(1),
            image3 = images.getOrNull(2),
            image4 = images.getOrNull(3),
            image5 = images.getOrNull(4),
            content = content,
            hashtag = hashtag
        )
        newStyle.updateStyleItems(itemIds!!)
        return newStyle
    }
}

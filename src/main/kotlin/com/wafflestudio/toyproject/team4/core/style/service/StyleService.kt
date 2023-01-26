package com.wafflestudio.toyproject.team4.core.style.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.style.api.response.StyleResponse
import com.wafflestudio.toyproject.team4.core.style.api.response.StylesResponse
import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity
import com.wafflestudio.toyproject.team4.core.style.database.StyleRepository
import com.wafflestudio.toyproject.team4.core.style.database.StyleRepositoryCustomImpl
import com.wafflestudio.toyproject.team4.core.style.domain.Style
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

interface StyleService {
    fun getStyles(index: Long, count: Long, sort: String?): StylesResponse
    fun getStyle(username: String?, styleId: Long): StyleResponse
}

@Service
class StyleServiceImpl(
    private val itemRepository: ItemRepository,
    private val styleRepository: StyleRepository,
    private val userRepository: UserRepository,
): StyleService {
    override fun getStyles(index: Long, count: Long, sort: String?): StylesResponse {
        val sortingMethod = StyleRepositoryCustomImpl.Sort.valueOf(sort ?: "recent")

        val allStyles = styleRepository.findAllOrderBy(sortingMethod)

        return StylesResponse(
            styles = allStyles
                .filterIndexed { idx, _ -> (idx / count) == index }
                .map { entity ->
                    Style.of(
                        entity = entity,
                        items = findStyleItems(entity)
                    )
                },
            totalPages = ceil(allStyles.size.toDouble() / count).toLong()
        )
    }

    @Transactional
    override fun getStyle(username: String?, styleId: Long): StyleResponse {
        val user = username?.let { userRepository.findByUsername(it) } // null인 경우 -> 액세스 토큰 전달 X

        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")

        return StyleResponse(
            style = Style.of(
                entity = style,
                items = findStyleItems(style)
            ),
            likedCount = style.likedUserCount,
            isFollow = false,
            isLike = false,
        )
    }

    private fun findStyleItems(style: StyleEntity): List<ItemEntity> {
        val styleItemIds = style.styleItems.map { it.itemId }
        return itemRepository.findAllByIds(styleItemIds)
    }
}
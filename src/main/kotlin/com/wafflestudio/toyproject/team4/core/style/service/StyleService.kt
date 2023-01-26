package com.wafflestudio.toyproject.team4.core.style.service

import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.style.api.response.StylesResponse
import com.wafflestudio.toyproject.team4.core.style.database.StyleRepository
import com.wafflestudio.toyproject.team4.core.style.database.StyleRepositoryCustomImpl
import com.wafflestudio.toyproject.team4.core.style.domain.Style
import org.springframework.stereotype.Service
import kotlin.math.ceil

interface StyleService {
    fun getStyles(index: Long, count: Long, sort: String?): StylesResponse
}

@Service
class StyleServiceImpl(
    private val itemRepository: ItemRepository,
    private val styleRepository: StyleRepository
): StyleService {
    override fun getStyles(index: Long, count: Long, sort: String?): StylesResponse {
        val sortingMethod = StyleRepositoryCustomImpl.Sort.valueOf(sort ?: "recent")

        val allStyles = styleRepository.findAllOrderBy(sortingMethod)

        return StylesResponse(
            styles = allStyles
                .filterIndexed { idx, _ -> (idx / count) == index }
                .map { entity ->
                    val itemIds = entity.styleItems.map { it.itemId }
                    Style.of(
                        entity = entity,
                        items = itemRepository.findAllByIds(itemIds)
                    )
                },
            totalPages = ceil(allStyles.size.toDouble() / count).toLong()
        )
    }
}
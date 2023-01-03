package com.wafflestudio.toyproject.team4.core.item.service

import com.wafflestudio.toyproject.team4.core.item.api.request.ItemRequest
import com.wafflestudio.toyproject.team4.core.item.api.response.ItemRankingResponse
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


interface ItemService {
    fun getItemRankingList(itemRequest: ItemRequest): ItemRankingResponse
}

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
) : ItemService {

    @Transactional(readOnly = true)
    override fun getItemRankingList(itemRequest: ItemRequest): ItemRankingResponse {
        val category = itemRequest.category
        val rankingList = with(itemRepository) {
            if (category == null) this.findAllByOrderByRatingDesc()
            else this.findAllByCategoryOrderByRatingDesc(Item.Category.valueOf(category))
        }
        val nextItemId = rankingList[0].nextItemId
        
        return ItemRankingResponse(
            items = rankingList.map { entity -> Item.of(entity) },
            nextItemId = nextItemId
        )
    }
}
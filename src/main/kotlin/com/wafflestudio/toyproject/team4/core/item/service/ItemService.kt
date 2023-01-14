package com.wafflestudio.toyproject.team4.core.item.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.api.response.ItemRankingResponse
import com.wafflestudio.toyproject.team4.core.item.api.response.ItemResponse
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


interface ItemService {
    fun getItemRankingList(category: String?, subCategory: String?, index: Long, count: Long): ItemRankingResponse
    fun getItem(itemId: Long): ItemResponse
}

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
) : ItemService {

    @Transactional(readOnly = true)
    override fun getItemRankingList(category: String?, subCategory: String?, index: Long, count: Long): ItemRankingResponse {
        val rankingList = with(itemRepository) {
            if(category.isNullOrEmpty() && subCategory.isNullOrEmpty()) {         // all items
                findAllByOrderByRatingDesc()
            } else if (!category.isNullOrEmpty() && subCategory.isNullOrEmpty()) { // category 전체
                findAllByCategoryOrderByRatingDesc(Item.Category.valueOf(category.uppercase()))
            } else { // subCategory is not null
                findAllBySubCategoryOrderByRatingDesc(Item.SubCategory.valueOf(subCategory!!.uppercase()))
            }
        }.filterIndexed { idx, _ -> (idx/count) == index}

        return ItemRankingResponse(
            items = rankingList.map { entity -> Item.of(entity) }
        )
    }

    override fun getItem(itemId: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomHttp404("존재하지 않는 상품 아이디입니다.")
        return ItemResponse(Item.of(item))
    }
}
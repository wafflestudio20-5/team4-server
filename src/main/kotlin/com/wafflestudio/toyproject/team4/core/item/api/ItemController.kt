package com.wafflestudio.toyproject.team4.core.item.api

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.item.api.response.ItemRankingResponse
import com.wafflestudio.toyproject.team4.core.item.service.ItemService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class ItemController(
    private val itemService: ItemService
) {
    
    @GetMapping("/items")
    fun getItemRankingList(
        @RequestParam category: String?,
        @RequestParam subcategory: String?,
        @RequestParam index: Long?,
        @RequestParam count: Long?,
    ) = itemService.getItemRankingList(
        category, subcategory,
        index?:0L, count?:20L
    )
    
    @GetMapping("/item/{id}")
    fun getItem(
        @PathVariable(value="id") itemId: Long
    ) = itemService.getItem(itemId)

    @GetMapping("/item/{id}/reviews")
    fun getItemReviews(
        @PathVariable(value="id") itemId: Long,
        @RequestParam index: Long?,
        @RequestParam count: Long?
    ) = itemService.getItemReviews(itemId, index?: 0L, count?: 5L)

    @GetMapping("/item/{id}/inquiries")
    fun getItemInquiries(
        @PathVariable(value="id") itemId: Long,
        @RequestParam index: Long?,
        @RequestParam count: Long?
    ) = itemService.getItemInquiries(itemId, index?: 0L, count?: 5L)


    @GetMapping("/search")
    fun searchItemByQuery(
        @RequestParam query: String?,
        @RequestParam index: Long?,
        @RequestParam count: Long?,
    ): ItemRankingResponse {
        if(query.isNullOrEmpty()) throw CustomHttp400("검색어를 입력하세요")
        else return itemService.searchItemByQuery(query, index?:0L, count?:20L)
    } 
}
package com.wafflestudio.toyproject.team4.core.item.api

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


    @GetMapping("/search")
    fun searchItemByQuery(
        @RequestParam query: String,
        @RequestParam index: Long?,
        @RequestParam count: Long?,
    ) = itemService.searchItemByQuery(query, index?:0L, count?:20L)
}
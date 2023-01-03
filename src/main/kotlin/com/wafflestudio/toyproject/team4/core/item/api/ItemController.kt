package com.wafflestudio.toyproject.team4.core.item.api

import com.wafflestudio.toyproject.team4.core.item.api.request.ItemRequest
import com.wafflestudio.toyproject.team4.core.item.service.ItemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class ItemController(
    private val itemService: ItemService
) {
    
    @GetMapping("/items/")
    fun getHomepage(
        @RequestBody itemRequest: ItemRequest
    ) = itemService.getItemRankingList(itemRequest)
}
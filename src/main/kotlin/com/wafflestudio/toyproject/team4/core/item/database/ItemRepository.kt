package com.wafflestudio.toyproject.team4.core.item.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ItemRepository : JpaRepository<ItemEntity, Long>, ItemRepositoryCustom {
    fun findAllByCategoryOrderByRatingDesc(category: Item.Category): List<ItemEntity>
    fun findAllByOrderByRatingDesc(): List<ItemEntity>
}

interface ItemRepositoryCustom {
    fun getItems(itemEntities: MutableList<ItemEntity>): MutableList<Item>
}

@Component
class ItemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ItemRepositoryCustom {
    override fun getItems(itemEntities: MutableList<ItemEntity>): MutableList<Item> {
        val result = mutableListOf<Item>()
        for (itemEntity in itemEntities) {
            result.add(Item.of(itemEntity))
        }
        return result
    }
}
package com.wafflestudio.toyproject.team4.core.item.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.item.database.QItemEntity.itemEntity
import com.wafflestudio.toyproject.team4.core.item.database.QItemImageEntity.itemImageEntity
import com.wafflestudio.toyproject.team4.core.item.database.QOptionEntity.optionEntity
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ItemRepository : JpaRepository<ItemEntity, Long>, ItemRepositoryCustom {
    fun findAllByNameContainingOrderByRatingDesc(query: String): List<ItemEntity>
    fun findAllByBrandContainingOrderByRatingDesc(query: String): List<ItemEntity>
}

interface ItemRepositoryCustom {
    fun findAllByOrderBy(category: Item.Category?, subCategory: Item.SubCategory?, sort: ItemRepositoryCustomImpl.Sort): List<ItemEntity>
}

@Component
class ItemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ItemRepositoryCustom {
    override fun findAllByOrderBy(category: Item.Category?, subCategory: Item.SubCategory?, sort: Sort): List<ItemEntity> {
        val eqInterest = if (category == null && subCategory == null) { null }
            else if (category != null && subCategory == null) { itemEntity.category.eq(category) }
            else { itemEntity.subCategory.eq(subCategory) }

        val ordering = when(sort) {
            Sort.PRICE -> itemEntity.newPrice.coalesce(itemEntity.oldPrice).asc()
            Sort.PRICE_REVERSE -> itemEntity.newPrice.coalesce(itemEntity.oldPrice).desc()
            Sort.SALE -> itemEntity.sale.desc()
            else -> itemEntity.rating.desc()
        }

        return queryFactory
            .select(itemEntity)
            .from(itemEntity)
            .leftJoin(itemImageEntity)
            .on(itemImageEntity.item.eq(itemEntity))
            .fetchJoin()
            .leftJoin(optionEntity)
            .on(optionEntity.item.eq(itemEntity))
            .fetchJoin()
            .where(eqInterest)
            .orderBy(ordering)
            .fetch()
    }


    enum class Sort {
        PRICE, PRICE_REVERSE, RATING, SALE
    }
}

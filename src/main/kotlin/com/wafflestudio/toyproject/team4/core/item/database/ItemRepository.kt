package com.wafflestudio.toyproject.team4.core.item.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.toyproject.team4.core.item.database.QItemEntity.itemEntity
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface ItemRepository : JpaRepository<ItemEntity, Long>, ItemRepositoryCustom

interface ItemRepositoryCustom {
    fun findAllByOrderBy(category: Item.Category?, subCategory: Item.SubCategory?, sort: ItemRepositoryCustomImpl.Sort): List<ItemEntity>
    fun findAllByContainingOrderByRatingDesc(query: String): List<ItemEntity>
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
            .selectDistinct(itemEntity)
            .from(itemEntity)
            .leftJoin(itemEntity.images).fetchJoin()
            .where(eqInterest)
            .orderBy(ordering)
            .fetch()
    }

    override fun findAllByContainingOrderByRatingDesc(query: String): List<ItemEntity> {
        // to give high priority to the results searched by name(=itemsSearchedByName),
        // simply add the results searched by brand(=itemsSearchedByBrand) afterwards
        val searchedItemList = findContainingQuery("name", query) + findContainingQuery("brand", query)
        // 품목명에도 query가 포함되고, 또 브랜드명에도 포함될 수 있음 -> distinct 처리 필요
        return searchedItemList.distinct()
    }

    private fun findContainingQuery(field: String, query: String): List<ItemEntity> {
        val eqInterest = when(field) {
            "name" -> itemEntity.name.contains(query)
            else -> itemEntity.brand.contains(query)
        }

        return queryFactory
            .selectDistinct(itemEntity)
            .from(itemEntity)
            .leftJoin(itemEntity.images).fetchJoin()
            .where(eqInterest)
            .orderBy(itemEntity.rating.desc())
            .fetch()
    }


    enum class Sort {
        PRICE, PRICE_REVERSE, RATING, SALE
    }
}

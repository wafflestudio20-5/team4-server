package com.wafflestudio.toyproject.team4.core.item.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.board.api.response.InquiriesResponse
import com.wafflestudio.toyproject.team4.core.board.api.response.ReviewsResponse
import com.wafflestudio.toyproject.team4.core.board.database.InquiryRepository
import com.wafflestudio.toyproject.team4.core.board.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.board.domain.Inquiry
import com.wafflestudio.toyproject.team4.core.board.domain.Review
import com.wafflestudio.toyproject.team4.core.item.api.request.PostItemInquiryRequest
import com.wafflestudio.toyproject.team4.core.item.api.response.ItemRankingResponse
import com.wafflestudio.toyproject.team4.core.item.api.response.ItemResponse
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepositoryCustomImpl
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.item.domain.RankingItem
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

interface ItemService {
    fun getItemRankingList(
        category: String?,
        subCategory: String?,
        index: Long,
        count: Long,
        sort: String?
    ): ItemRankingResponse
    fun getItem(itemId: Long): ItemResponse
    fun getItemReviews(itemId: Long, index: Long, count: Long): ReviewsResponse
    fun getItemInquiries(itemId: Long, index: Long, count: Long): InquiriesResponse
    fun postItemInquiry(username: String, itemId: Long, postItemInquiryRequest: PostItemInquiryRequest)

    fun searchItemByQuery(query: String, index: Long, count: Long): ItemRankingResponse
}

@Service
class ItemServiceImpl(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
    private val reviewRepository: ReviewRepository,
    private val inquiryRepository: InquiryRepository
) : ItemService {
    @Transactional(readOnly = true)
    override fun getItemRankingList(
        category: String?,
        subCategory: String?,
        index: Long,
        count: Long,
        sort: String?
    ): ItemRankingResponse {
        val itemCategory = category?.let { Item.Category.valueOf(camelToUpper(it)) }
        val itemSubCategory = subCategory?.let { Item.SubCategory.valueOf(camelToUpper(it)) }
        val sortingMethod = ItemRepositoryCustomImpl.Sort.valueOf(camelToUpper(sort ?: "rating"))

        val rankingList = itemRepository.findAllByOrderBy(itemCategory, itemSubCategory, index, count, sortingMethod)
        val totalCount = itemRepository.getTotalCount(itemCategory, itemSubCategory)

        return ItemRankingResponse(
            items = rankingList.map { entity -> RankingItem.of(entity) },
            totalPages = ceil(totalCount.toDouble() / count).toLong()
        )
    }

    private fun camelToUpper(camelCaseWord: String): String {
        return """([a-z])([A-Z]+)""".toRegex().replace(camelCaseWord, "$1_$2").uppercase()
    }

    override fun getItem(itemId: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomHttp404("존재하지 않는 상품 아이디입니다.")

        return ItemResponse(Item.of(item))
    }

    override fun getItemReviews(itemId: Long, index: Long, count: Long): ReviewsResponse {
        val itemReviews = reviewRepository.findAllByItemIdOrderByRatingDesc(itemId)

        return ReviewsResponse(
            reviews = itemReviews
                .filterIndexed { idx, _ -> (idx / count) == index }
                .map { entity -> Review.of(entity) }
        )
    }

    override fun getItemInquiries(itemId: Long, index: Long, count: Long): InquiriesResponse {
        val itemInquiries = inquiryRepository.findAllByItem_IdOrderByCreatedDateTimeDesc(itemId, index, count)
        val itemTotalInquiryCount = inquiryRepository.getItemTotalInquiryCount(itemId)

        return InquiriesResponse(
            inquiries = itemInquiries.map { entity -> Inquiry.of(entity) },
            totalPages = ceil(itemTotalInquiryCount.toDouble() / count).toLong()
        )
    }

    @Transactional
    override fun postItemInquiry(username: String, itemId: Long, postItemInquiryRequest: PostItemInquiryRequest) {
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")
        val user = userRepository.findByUsername(username)!!

        // check whether the option that user selected is one of the option of the item
        val newOption = postItemInquiryRequest.option
        val itemOptions = item.options?.map { it.optionName } ?: listOf()
        if (!newOption.isNullOrEmpty() && !itemOptions.contains(newOption))
            throw CustomHttp400("상품에 존재하지 않는 옵션입니다.")

        // make a new ItemInquiry object
        item.writeInquiry(user, postItemInquiryRequest)
    }

    override fun searchItemByQuery(query: String, index: Long, count: Long): ItemRankingResponse {
        val itemList = itemRepository.findAllByContainingOrderByRatingDesc(query)

        return ItemRankingResponse(
            items = itemList
                .filterIndexed { idx, _ -> (idx / count) == index }
                .map { entity -> RankingItem.of(entity) },
            totalPages = ceil(itemList.size.toDouble() / count).toLong()
        )
    }
}

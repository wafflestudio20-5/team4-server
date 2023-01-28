package com.wafflestudio.toyproject.team4.core.style.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.style.api.request.PostStyleRequest
import com.wafflestudio.toyproject.team4.core.style.api.response.StyleResponse
import com.wafflestudio.toyproject.team4.core.style.api.response.StylesResponse
import com.wafflestudio.toyproject.team4.core.style.database.*
import com.wafflestudio.toyproject.team4.core.style.domain.Style
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

interface StyleService {
    fun getStyles(index: Long, count: Long, sort: String?): StylesResponse
    fun getStyle(username: String?, styleId: Long): StyleResponse
    fun postStyle(username: String, postStyleRequest: PostStyleRequest)
    fun postLike(username: String, styleId: Long)
}

@Service
class StyleServiceImpl(
    private val itemRepository: ItemRepository,
    private val styleRepository: StyleRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
) : StyleService {
    override fun getStyles(index: Long, count: Long, sort: String?): StylesResponse {
        val sortingMethod = StyleRepositoryCustomImpl.Sort.valueOf(sort?.uppercase() ?: "RECENT")
        val allStyles = styleRepository.findAllOrderBy(sortingMethod)

        return StylesResponse(
            styles = allStyles
                .filterIndexed { idx, _ -> (idx / count) == index }
                .map { entity ->
                    Style.of(
                        entity = entity,
                        items = findStyleItems(entity)
                    )
                },
            totalPages = ceil(allStyles.size.toDouble() / count).toLong()
        )
    }

    @Transactional
    override fun getStyle(username: String?, styleId: Long): StyleResponse {
        val user = username?.let { userRepository.findByUsername(it) }
        if (username != null && user == null) throw CustomHttp400("INVALID ACCESS TOKEN")

        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")

        val isFollow = user?.let { followRepository.findRelation(it.id, style.user.id) } ?: false
        val likedUserIds = style.likedUsers.map { it.userId }
        val isLike = user?.let { likedUserIds.contains(it.id) } ?: false

        return StyleResponse(
            style = Style.of(
                entity = style,
                items = findStyleItems(style)
            ),
            likedCount = style.likedUserCount,
            isFollow = isFollow,
            isLike = isLike,
        )
    }

    private fun findStyleItems(style: StyleEntity): List<ItemEntity> {
        val styleItemIds = style.styleItems.map { it.itemId }
        return itemRepository.findAllByIds(styleItemIds)
    }

    @Transactional
    override fun postStyle(username: String, postStyleRequest: PostStyleRequest) {
        val user = userRepository.findByUsername(username)!!
        val style = postStyleRequest.toEntity(user)

        user.styles.add(style)
    }

    @Transactional
    override fun postLike(username: String, styleId: Long) {
        val user = userRepository.findByUsername(username)!!
        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")
        val likedUser = style.likedUsers.find { it.userId == user.id }
        if (likedUser == null) {
            style.addLikedUser(user.id)
            return
        }
        if (!likedUser.isActive)
            throw CustomHttp400("이미 좋아요를 눌렀습니다.")
        likedUser.changeActive()
    }
}

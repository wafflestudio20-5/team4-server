package com.wafflestudio.toyproject.team4.core.style.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp403
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.style.api.request.PatchStyleRequest
import com.wafflestudio.toyproject.team4.core.style.api.request.PostStyleRequest
import com.wafflestudio.toyproject.team4.core.style.api.response.StyleResponse
import com.wafflestudio.toyproject.team4.core.style.api.response.StylesResponse
import com.wafflestudio.toyproject.team4.core.style.api.response.UserStylesResponse
import com.wafflestudio.toyproject.team4.core.style.database.StyleEntity
import com.wafflestudio.toyproject.team4.core.style.database.StyleRepository
import com.wafflestudio.toyproject.team4.core.style.database.StyleRepositoryCustomImpl
import com.wafflestudio.toyproject.team4.core.style.domain.Style
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.math.ceil

interface StyleService {
    fun getStyles(index: Long, count: Long, sort: String?): StylesResponse
    fun getStyle(username: String?, styleId: Long): StyleResponse
    fun getUserStyles(userId: Long): UserStylesResponse
    fun postStyle(username: String, postStyleRequest: PostStyleRequest)
    fun patchStyle(username: String, styleId: Long, patchStyleRequest: PatchStyleRequest)
    fun deleteStyle(username: String, styleId: Long)
    fun postLike(username: String, styleId: Long)
    fun deleteLike(username: String, styleId: Long)
}

@Service
class StyleServiceImpl(
    private val itemRepository: ItemRepository,
    private val styleRepository: StyleRepository,
    private val userRepository: UserRepository,
    private val userService: UserService
) : StyleService {
    override fun getStyles(index: Long, count: Long, sort: String?): StylesResponse {
        val sortingMethod = StyleRepositoryCustomImpl.Sort.valueOf(sort?.uppercase() ?: "LIKE")
        val allStyles = styleRepository.findAllOrderBy(index, count, sortingMethod)
        val totalCount = styleRepository.getTotalCount()

        return StylesResponse(
            styles = allStyles
                .map { entity ->
                    Style.of(
                        entity = entity,
                        items = findStyleItems(entity)
                    )
                },
            totalPages = ceil(totalCount.toDouble() / count).toLong()
        )
    }

    @Transactional
    override fun getStyle(username: String?, styleId: Long): StyleResponse {
        val user = username?.let { userRepository.findByUsername(it) }
        if (username != null && user == null) throw CustomHttp400("INVALID ACCESS TOKEN")

        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")

        val isFollow = userService.getIsFollow(user, style.user)
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

    @Transactional
    override fun getUserStyles(userId: Long): UserStylesResponse {
        val user = userRepository.findByIdOrNullWithStylesOrderByRecentDesc(userId)
            ?: throw CustomHttp404("존재하지 않는 사용자입니다.")

        return UserStylesResponse(user.styles.map { Style.preview(it) })
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
    override fun patchStyle(username: String, styleId: Long, patchStyleRequest: PatchStyleRequest) {
        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")
        if (username != style.user.username) {
            throw CustomHttp403("수정 권한이 없습니다.")
        }

        style.update(patchStyleRequest)
    }

    @Transactional
    override fun deleteStyle(username: String, styleId: Long) {
        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")
        if (username != style.user.username) {
            throw CustomHttp403("삭제 권한이 없습니다.")
        }

        styleRepository.delete(style)
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
        if (likedUser.isActive)
            throw CustomHttp400("이미 좋아요를 눌렀습니다.")
        likedUser.changeActive()
    }

    @Transactional
    override fun deleteLike(username: String, styleId: Long) {
        val user = userRepository.findByUsername(username)!!
        val style = styleRepository.findByIdOrNull(styleId)
            ?: throw CustomHttp404("존재하지 않는 스타일입니다.")
        val likedUser = style.likedUsers.find { it.userId == user.id }
        if (likedUser == null || !likedUser.isActive)
            throw CustomHttp400("좋아요를 누른 스타일이 아닙니다.")
        likedUser.changeActive()
    }
}

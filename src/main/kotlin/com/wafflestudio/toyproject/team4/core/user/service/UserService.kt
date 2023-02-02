package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.board.api.response.InquiriesResponse
import com.wafflestudio.toyproject.team4.core.board.database.InquiryRepository
import com.wafflestudio.toyproject.team4.core.board.domain.Inquiry
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.user.database.FollowEntity
import com.wafflestudio.toyproject.team4.core.user.database.FollowRepository
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.user.api.response.*
import com.wafflestudio.toyproject.team4.core.user.database.RecentItemRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.domain.RecentItem
import com.wafflestudio.toyproject.team4.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.math.ceil

interface UserService {
    fun getMe(username: String): UserMeResponse
    fun patchMe(username: String, patchMeRequest: PatchMeRequest)

    fun getUser(username: String?, userId: Long): UserResponse

    fun getFollowers(userId: Long): FollowersResponse
    fun getFollowings(userId: Long): FollowingsResponse
    fun getIsFollow(currentUser: UserEntity?, closetOwner: UserEntity): Boolean
    fun follow(username: String, userId: Long)
    fun unfollow(username: String, userId: Long)

    fun searchUsers(query: String?, index: Long, count: Long): UserSearchResponse

    fun getRecentlyViewed(username: String): RecentItemsResponse
    fun postRecentlyViewed(username: String, itemId: Long)

    fun getItemInquiries(username: String, index: Long, count: Long): InquiriesResponse
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val recentItemRepository: RecentItemRepository,
    private val itemRepository: ItemRepository,
    private val inquiryRepository: InquiryRepository,
    private val followRepository: FollowRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    @Transactional
    override fun getMe(username: String): UserMeResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return UserMeResponse(User.of(user))
    }

    @Transactional
    override fun patchMe(username: String, patchMeRequest: PatchMeRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val newEncodedPassword = patchMeRequest.password?.let { passwordEncoder.encode(it) }
        user.update(patchMeRequest, newEncodedPassword)
    }

    /* **********************************************************
    //                       My Closet                         //
    ********************************************************** */

    @Transactional
    override fun getUser(username: String?, userId: Long): UserResponse {
        val currentUser = username?.let { userRepository.findByUsernameOrNullWithFollows(it) }
        val closetOwner = userRepository.findByIdOrNullWithFollows(userId)
            ?: throw CustomHttp404("존재하지 않는 사용자입니다.")

        val styleCount: Long = closetOwner.styles.size.toLong()
        val followerCount: Long = closetOwner.followers.count { it.isActive }.toLong()
        val followingCount: Long = closetOwner.followings.count { it.isActive }.toLong()
        val isFollow = getIsFollow(currentUser, closetOwner)

        return UserResponse(
            user = User.of(closetOwner),
            count = Count(styleCount, followerCount, followingCount),
            isFollow = isFollow,
        )
    }

    /* **********************************************************
    //                         Follow                          //
    ********************************************************** */

    override fun getIsFollow(currentUser: UserEntity?, closetOwner: UserEntity): Boolean {
        val relationHistory = currentUser?.followings?.find { it.followed == closetOwner }

        return relationHistory?.isActive ?: false
    }

    override fun getFollowers(userId: Long): FollowersResponse {
        val closetOwner = userRepository.findByIdOrNullWithFollowersWithUsers(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followers = closetOwner.followers.map { User.simplify(it.following) }

        return FollowersResponse(followers)
    }

    override fun getFollowings(userId: Long): FollowingsResponse {
        val closetOwner = userRepository.findByIdOrNullWithFollowingsWithUsers(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followings = closetOwner.followings.map { User.simplify(it.followed) }

        return FollowingsResponse(followings)
    }

    @Transactional
    override fun follow(username: String, userId: Long) {
        val followingUser = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followedUser = userRepository.findByIdOrNull(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val relation = followRepository.findRelation(followingUser, followedUser)
        if (relation == null) followRepository.save(FollowEntity(followingUser, followedUser))
        else relation.activate()
    }

    @Transactional
    override fun unfollow(username: String, userId: Long) {
        val followingUser = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val followedUser = userRepository.findByIdOrNull(userId)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val follow = followRepository.findRelation(followingUser, followedUser)
            ?: throw CustomHttp404("해당 사용자를 팔로우하고 있지 않습니다.")

        follow.deactivate()
    }

    /* **********************************************************
    //                      Search User                        //
    ********************************************************** */

    override fun searchUsers(query: String?, index: Long, count: Long): UserSearchResponse {
        if (query == "" || query == null) throw CustomHttp400("검색어를 입력하세요.")
        val users = userRepository.searchByQuery(query, index, count)

        return UserSearchResponse(users.map { User.simplify(it) })
    }

    /* **********************************************************
    //                    Recently Viewed                      //
    ********************************************************** */

    @Transactional
    override fun getRecentlyViewed(username: String): RecentItemsResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val recentItems = recentItemRepository
            .findAllByUserOrderByViewedDateTimeDesc(user)
            .groupBy({ it.item }, { it })
            .map { entry -> entry.value.maxBy { it.id } }
            .filterIndexed { idx, _ -> idx < 12 }

        return RecentItemsResponse(
            recentItems = recentItems.map { entity -> RecentItem.of(entity) }
        )
    }

    @Transactional
    override fun postRecentlyViewed(username: String, itemId: Long) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")

        user.viewItem(item)
    }

    /* **********************************************************
    //                     Item Inquiries                      //
    ********************************************************** */

    @Transactional
    override fun getItemInquiries(username: String, index: Long, count: Long): InquiriesResponse {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val itemInquiries = inquiryRepository.findAllByUserOrderByCreatedDateTimeDesc(user, index, count)
        return InquiriesResponse(
            inquiries = itemInquiries.map { entity -> Inquiry.of(entity) },
            totalPages = ceil(user.itemInquiries.size.toDouble() / count).toLong()
        )
    }
}

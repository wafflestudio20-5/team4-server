package com.wafflestudio.toyproject.team4.core.style.database

import com.wafflestudio.toyproject.team4.core.style.api.request.PatchStyleRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "styles")
class StyleEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val user: UserEntity,

    var image1: String,
    var image2: String? = null,
    var image3: String? = null,
    var image4: String? = null,
    var image5: String? = null,

    var content: String? = null,
    var hashtag: String? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "style", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var styleItems: MutableList<ItemStyleEntity> = mutableListOf()

    @OneToMany(mappedBy = "likedStyle", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val likedUsers: MutableList<UserLikedStyleEntity> = mutableListOf()
    var likedUserCount: Long = 0L

    fun updateStyleItems(itemIds: List<Long>) {
        this.styleItems = itemIds
            .map { itemId -> ItemStyleEntity(itemId, this) }
            .toMutableList()
    }

    fun update(request: PatchStyleRequest) {
        if (request.images != null) {
            this.image1 = request.images.first()
            this.image2 = request.images.getOrNull(1)
            this.image3 = request.images.getOrNull(2)
            this.image4 = request.images.getOrNull(3)
            this.image5 = request.images.getOrNull(4)
        }
        this.styleItems = request.itemIds
            ?.map { itemId -> ItemStyleEntity(itemId, this) }
            ?.toMutableList()
            ?: this.styleItems
        this.content = request.content ?: this.content
        this.hashtag = request.hashtag ?: this.hashtag

    fun addLikedUser(userId: Long): UserLikedStyleEntity {
        val userLikedStyle = UserLikedStyleEntity(
            userId = userId,
            likedStyle = this,
        )
        this.likedUsers.add(userLikedStyle)
        this.likedUserCount++
        return userLikedStyle
    }
}

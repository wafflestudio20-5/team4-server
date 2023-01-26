package com.wafflestudio.toyproject.team4.core.style.database

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

    var content: String,
    var hashtag: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "style", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val styleItems: MutableList<ItemStyleEntity> = mutableListOf()

    @OneToMany(mappedBy = "likedStyle", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val likedUsers: MutableList<UserLikedStyleEntity> = mutableListOf()
    var likedUserCount: Long? = 0L
}

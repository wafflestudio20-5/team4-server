package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import java.time.LocalDateTime

data class User(
    val id: Long,
    val username: String,
    val nickname: String,
    var imageUrl: String? = null,
    var registrationDate: LocalDateTime,
    var height: Long? = null,
    var weight: Long? = null,
    val sex: Sex? = null,
//    var socialKey: String? = null,

//    val reviews: List<Review>,
//    val purchases: List<Item>,
//    val shoppingCart: List<Item>,
//    val recentlyViewed: List<Item>
) {
    enum class Sex {
        MALE, FEMALE
    }

    companion object {
        fun of(entity: UserEntity): User = entity.run {
            User(
                id = id,
                username = username,
                nickname = nickname,
                imageUrl = imageUrl,
                registrationDate = registrationDate,
                height = height,
                weight = weight,
                sex = sex
            )
        }
    }
}
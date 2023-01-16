package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import java.time.LocalDateTime

data class User(
    val id: Long,
    val username: String,
    val nickname: String,
    var image: String? = null,
    var reviewCount: Long?,
    var registrationDate: LocalDateTime,
    var height: Long? = null,
    var weight: Long? = null,
    val sex: String? = null,
) {
    enum class Sex {
        MALE, FEMALE
    }

    enum class Role {
        ROLE_USER, ROLE_ADMIN
    }

    companion object {
        fun of(entity: UserEntity): User = entity.run {
            User(
                id = id,
                username = username,
                nickname = nickname,
                image = image,
                reviewCount = reviewCount,
                registrationDate = registrationDate,
                height = height,
                weight = weight,
                sex = sex.toString().lowercase(),
            )
        }
    }
}
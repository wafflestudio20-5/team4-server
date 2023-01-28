package com.wafflestudio.toyproject.team4.core.user.domain

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import java.time.LocalDateTime

data class User(
    val id: Long,
    val username: String,
    var nickname: String,
    var image: String?,
    var reviewCount: Long,
    val registrationDate: LocalDateTime,
    var height: Long? = null,
    var weight: Long? = null,
    var sex: String? = null,
    var description: String? = null,
    var instaUsername: String? = null,
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
                description = description,
                instaUsername = instaUsername
            )
        }
    }
}

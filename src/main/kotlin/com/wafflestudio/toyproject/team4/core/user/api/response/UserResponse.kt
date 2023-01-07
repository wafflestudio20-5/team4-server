package com.wafflestudio.toyproject.team4.core.user.api.response

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import com.wafflestudio.toyproject.team4.core.user.domain.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val username: String,
    val nickname: String,
    val imageUrl: String?,
    val registrationDate: LocalDateTime,
    val height: Long?,
    val weight: Long?,
    val sex: User.Sex?,

) {    
    var reviews: MutableList<ReviewResponse>? = null
    
    companion object {
        fun of(userEntity: UserEntity) =
            UserResponse(
                id = userEntity.id,
                username = userEntity.username,
                nickname = userEntity.nickname,
                imageUrl = userEntity.imageUrl,
                registrationDate = userEntity.registrationDate,
                height = userEntity.height,
                weight = userEntity.weight,
                sex = userEntity.sex,
            )
    }
}
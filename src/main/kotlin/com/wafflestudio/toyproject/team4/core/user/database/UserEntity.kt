package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.user.domain.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    @Column(unique = true)
    val username: String,
    var encodedPassword: String,

    @Column(unique = true)
    var nickname: String,
    var imageUrl: String? = null,
    var reviewCount: Long? = 0L,

    @Enumerated(EnumType.STRING)
    val sex: User.Sex? = null,
    var height: Long? = null,
    var weight: Long? = null,
    var socialKey: String? = null,

    var refreshToken: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var registrationDate: LocalDateTime = LocalDateTime.now()


}
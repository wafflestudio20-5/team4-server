package com.wafflestudio.toyproject.team4.core.user.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener::class)
class UserEntity (
    val username: String,
    var encodedPassword: String,
    var nickname: String,
    var imageURL: String? = null,
    var reviewCount: Long? = 0L,
    
    val sex: String? = null,
    var height: Long? = null,
    var weight: Long? = null,
    
    var socialKey: String? = null,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    @CreatedDate
    val registrationDate: LocalDateTime = LocalDateTime.now()
}
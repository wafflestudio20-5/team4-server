package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "inquiries")
class InquiryEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val item: ItemEntity,
    
    var title: String,
    var content: String,
    val type: Type,
    var isSecret: Boolean,
    val isAnswered: Boolean
) {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @CreatedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    
    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<InquiryImageEntity> = mutableListOf()

    
    enum class Type {
        SIZE, DELIVERY, RESTOCK, DETAIL
    }
    
}
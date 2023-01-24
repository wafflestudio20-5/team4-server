package com.wafflestudio.toyproject.team4.core.board.database

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.user.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "inquiries")
@EntityListeners(AuditingEntityListener::class)
class InquiryEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val item: ItemEntity,

    var title: String,
    var content: String,
    @Enumerated(EnumType.STRING)
    var type: Type,
    var optionName: String? = null,
    var isSecret: Boolean,
    val isAnswered: Boolean
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<InquiryImageEntity> = mutableListOf()

    fun update(
        request: PutItemInquiriesRequest
    ) {
        val itemOptionList = item.options!!.map { it.optionName }
        if (!request.option.isNullOrEmpty() && !itemOptionList.contains(request.option))
            throw CustomHttp400("유효하지 않은 상품 옵션입니다.")

        this.title = request.title!!
        this.content = request.content!!
        this.optionName = request.option
        this.type = Type.valueOf(request.type.uppercase())
        this.isSecret = request.isSecret
        if (request.images != null) {
            this.images = request.images.map { url ->
                InquiryImageEntity(this, url)
            }.toMutableList()
        }
    }

    enum class Type {
        SIZE, DELIVERY, RESTOCK, DETAIL
    }
}

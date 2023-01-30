package com.wafflestudio.toyproject.team4.core.board.database

import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.core.item.database.ItemEntity
import com.wafflestudio.toyproject.team4.core.user.api.request.PutItemInquiriesRequest
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

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

    var image1: String? = null,
    var image2: String? = null,
    var image3: String? = null,

    var isSecret: Boolean,
    val isAnswered: Boolean
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime? = null

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

        this.image1 = request.images?.getOrNull(0)
        this.image2 = request.images?.getOrNull(1)
        this.image3 = request.images?.getOrNull(2)
    }

    enum class Type {
        SIZE, DELIVERY, RESTOCK, DETAIL
    }
}

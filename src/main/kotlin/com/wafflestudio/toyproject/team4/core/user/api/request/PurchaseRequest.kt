package com.wafflestudio.toyproject.team4.core.user.api.request

data class PurchaseRequest(
    val id: Long,  //id of purchasing item
    val option: String?,
    val payment: Long,
    val quantity: Long,
)

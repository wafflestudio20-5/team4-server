package com.wafflestudio.toyproject.team4.core.user.api.request

data class PostShoppingCartRequest(
    val id: Long,
    val option: String? = null,
    val quantity: Long
)
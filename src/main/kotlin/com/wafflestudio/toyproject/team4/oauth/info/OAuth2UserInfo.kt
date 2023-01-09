package com.wafflestudio.toyproject.team4.oauth.info

abstract class OAuth2UserInfo(
    var attributes: Map<String, Any>
) {
    abstract val id: String
    abstract val imageUrl: String?
}
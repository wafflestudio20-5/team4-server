package com.wafflestudio.toyproject.team4.oauth.info.impl

import com.wafflestudio.toyproject.team4.oauth.info.OAuth2UserInfo

class KakaoOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override val id: String
        get() = attributes["id"].toString()
    override val image: String
        get() {
            val properties = attributes["properties"] as Map<String, Any>
            return properties["thumbnail_image"].toString()
        }
}

package com.wafflestudio.toyproject.team4.oauth.info

import com.wafflestudio.toyproject.team4.oauth.entity.ProviderType
import com.wafflestudio.toyproject.team4.oauth.info.impl.KakaoOAuth2UserInfo

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(providerType: ProviderType?, attributes: Map<String, Any>): OAuth2UserInfo {
        return when (providerType) {
            ProviderType.KAKAO -> KakaoOAuth2UserInfo(attributes)
            else -> throw IllegalArgumentException("Invalid Provider Type.")
        }
    }
}

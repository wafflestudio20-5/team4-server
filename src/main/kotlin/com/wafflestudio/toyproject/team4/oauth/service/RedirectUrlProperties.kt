package com.wafflestudio.toyproject.team4.oauth.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("social.login")
data class RedirectUrlProperties(
    val redirectUrl: String
)

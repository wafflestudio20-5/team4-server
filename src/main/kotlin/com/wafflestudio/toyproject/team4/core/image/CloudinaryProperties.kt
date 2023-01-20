package com.wafflestudio.toyproject.team4.core.image

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("cloudinary")
class CloudinaryProperties(
    val apiEnvironment: String
)

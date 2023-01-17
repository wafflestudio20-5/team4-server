package com.wafflestudio.toyproject.team4.core.image.service

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.wafflestudio.toyproject.team4.common.CustomHttp400
import com.wafflestudio.toyproject.team4.common.CustomHttp502
import com.wafflestudio.toyproject.team4.core.image.CloudinaryProperties
import com.wafflestudio.toyproject.team4.core.image.api.response.ImageUploadResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@EnableConfigurationProperties(CloudinaryProperties::class)
class ImageService(
    cloudinaryProperties: CloudinaryProperties
) {
    val cloudinary = Cloudinary(cloudinaryProperties.apiEnvironment)

    fun uploadImages(images: List<MultipartFile>): ImageUploadResponse {
        try {
            val secureImages = images.map {
                val base64Code = Base64.getEncoder().encodeToString(it.bytes)
                val base64DataUri = "data:image/png;base64,$base64Code"
                val uploadResult = cloudinary.uploader().upload(base64DataUri, ObjectUtils.emptyMap())
                uploadResult["secure_url"] as String
            }
            return ImageUploadResponse(secureImages)
        } catch (e: RuntimeException) {
            println(e)
            throw CustomHttp400("이미지 업로드에 실패했습니다.")
        } catch (e: Exception) {
            println(e)
            throw CustomHttp502("[서버 오류] 이미지 업로드에 실패했습니다.")
        }
    }

    fun getDefaultImage(username: String): String {
        val firstLetter = username.find { it.isLetter() }?.lowercase() ?: "a"
        return "//image.msscdn.net/mfile_s01/_simbols/_basic/$firstLetter.png"
    }
} 
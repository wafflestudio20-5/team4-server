package com.wafflestudio.toyproject.team4.core.image.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.core.image.service.ImageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/image-upload")
class ImageController(
    private val imageService: ImageService
) {
    @Authenticated
    @PostMapping
    fun uploadImages(
        @RequestPart images: List<MultipartFile>
    ) = imageService.uploadImages(images)
}

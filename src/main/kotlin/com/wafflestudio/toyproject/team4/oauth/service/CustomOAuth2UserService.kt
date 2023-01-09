package com.wafflestudio.toyproject.team4.oauth.service

import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.oauth.entity.ProviderType
import com.wafflestudio.toyproject.team4.oauth.info.OAuth2UserInfo
import com.wafflestudio.toyproject.team4.oauth.info.OAuth2UserInfoFactory
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest) // OAuth 서비스(github, google, naver)에서 가져온 유저 정보를 담고있음

        return try {
            this.process(userRequest, oAuth2User)
        } catch (e: AuthenticationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw InternalAuthenticationServiceException(e.message, e.cause)
        }
    }

    private fun process(userRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val providerType =
            ProviderType.valueOf(userRequest.clientRegistration.registrationId.uppercase(Locale.getDefault())) // OAuth 서비스 이름(ex. GITHUB, NAVER, GOOGLE)
        val oAuth2UserId = oAuth2User.attributes["id"]
        val username = "$providerType-$oAuth2UserId" // Arbitrary username Ex) KAKAO-1294726
        val userInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2User.attributes)
        var savedUser = userRepository.findByUsername(username)

        if (savedUser != null) {
            if (!savedUser.imageUrl.equals(userInfo.imageUrl))
                savedUser.imageUrl = userInfo.imageUrl // Update image
        } else {
            savedUser = UserEntity(
                username = username,
                encodedPassword = "NONE", // Arbitrary meaningless password
                nickname = username,
                imageUrl = userInfo.imageUrl,
                socialKey = providerType
            )
            userRepository.save(savedUser)
        }

        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority(savedUser.role.toString())),
            oAuth2User.attributes,
            userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
        )
    }
}
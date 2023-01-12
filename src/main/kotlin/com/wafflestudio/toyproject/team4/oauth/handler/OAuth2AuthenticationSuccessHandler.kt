package com.wafflestudio.toyproject.team4.oauth.handler

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.service.AuthTokenService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

@Component
class OAuth2AuthenticationSuccessHandler(
    val authTokenService: AuthTokenService,
    val userRepository: UserRepository
) : SimpleUrlAuthenticationSuccessHandler() {
    @Transactional
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User: OAuth2User = authentication.principal as OAuth2User
        val providerType = (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId.uppercase()
        val username = "$providerType-${oAuth2User.attributes["id"]}"
        val user = userRepository.findByUsername(username) ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        val accessToken = authTokenService.generateAccessTokenByUsername(username).accessToken
        val refreshToken = authTokenService.generateRefreshTokenByUsername(username)
        user.refreshToken = refreshToken
        val cookie = authTokenService.generateCookie(refreshToken)
        response.addCookie(cookie)

        val redirectUrl = makeRedirectUrl(accessToken)
        redirectStrategy.sendRedirect(request, response, redirectUrl)
    }

    private fun makeRedirectUrl(accessToken: String): String {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect/$accessToken")
            .build().toUriString()
    }
}
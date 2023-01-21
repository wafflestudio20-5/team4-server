package com.wafflestudio.toyproject.team4.oauth.handler

import com.wafflestudio.toyproject.team4.core.user.service.AuthTokenService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
    val authTokenService: AuthTokenService
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User: OAuth2User = authentication.principal as OAuth2User
        val providerType = (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId.lowercase()
        val username = "${providerType}_${oAuth2User.attributes["id"]}"
        val accessToken = authTokenService.generateAccessTokenByUsername(username).accessToken
        val redirectUrl = makeRedirectUrl(accessToken)
        redirectStrategy.sendRedirect(request, response, redirectUrl)
    }

    private fun makeRedirectUrl(accessToken: String): String {
        return UriComponentsBuilder.fromUriString(
            "https://musin4.netlify.app/oauth2/redirect/$accessToken"
        ).build().toUriString()
    }
}

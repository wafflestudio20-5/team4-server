package com.wafflestudio.toyproject.team4.core.user.service

import com.wafflestudio.toyproject.team4.common.Seminar400
import com.wafflestudio.toyproject.team4.common.Seminar401
import com.wafflestudio.toyproject.team4.core.user.api.response.AuthToken
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    fun generateTokenByUsername(username: String): AuthToken {
        val claims: Claims = Jwts.claims()
        claims["username"] = username
        val issuer = authProperties.issuer
        val expiryDate: Date = Date.from(
            LocalDateTime
                .now()
                .plusSeconds(authProperties.jwtExpiration)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )
        val resultToken = Jwts.builder()
            .setClaims(claims)
            .setIssuer(issuer)
            .setExpiration(expiryDate)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()

        return AuthToken(resultToken)
    }

    fun generateRefreshTokenByUsername(username: String): String {
        val claims: Claims = Jwts.claims()
        claims["username"] = username
        val issuer = authProperties.issuer
        val expiryDate: Date = Date.from(
            LocalDateTime
                .now()
                .plusSeconds(authProperties.refreshExpiration)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )
        return Jwts.builder()
            .setClaims(claims)
            .setIssuer(issuer)
            .setExpiration(expiryDate)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromToken(authToken: String): String {
        return try {
            parse(authToken).body["username"] as String
        } catch (e: ExpiredJwtException) {
            throw Seminar401("토큰 인증 시간이 만료되었습니다.")
        } catch (e: Exception) {
            throw Seminar400("INVALID TOKEN")
        }
    }

    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts
            .parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(prefixRemoved)
    }

    fun generateCookie(token: String): ResponseCookie {
        return ResponseCookie.from("refreshToken", token)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(3600)
            .build()
    }
}
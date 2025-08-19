package com.service.user.security

import com.service.user.model.dto.UserDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secret: String,
    @Value("\${jwt.access-token-expiration}")
    private val validityAccessTime: Long,
    @Value("\${jwt.refresh-token-expiration}")
    private val validityRefreshTime: Long
){

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    // Access/Refresh Token 생성
    fun createToken(flag:String, dto: UserDto): String {
        val claims: Claims = Jwts.claims().setSubject(dto.userId) // Token에 사용자 아이디 추가
        claims["ROLE"] = dto.userType
        val now = Date()

        // 토큰 타입에 따라 만료 시간 설정
        val expirationTime = if (flag == "access") Date(now.time + validityAccessTime) else Date(now.time + validityRefreshTime)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expirationTime)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    // Token 유효성 검증
    fun validateToken(token: String, adminId: String, adminRole: String): Boolean {
        return !isTokenExpired(token) && adminId == getAdminId(token) && adminRole == getAdminRole(token)
    }

    // Token에서 아이디 추출
    fun getAdminId(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    // Token에서 권한 추출
    fun getAdminRole(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body["ROLE"] as String
    }

    // Token 만료시간 검사
    fun isTokenExpired(token: String): Boolean {
        val expiration = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .expiration

        return expiration.before(Date())
    }

    // refresh 토큰 저장하는 쿠키 생성
    fun createRefreshTokenCookie(refreshToken: String): ResponseCookie {
        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
//            .secure(true)   // https
            .path("/")
            .maxAge(validityRefreshTime/1000)   // 초 단위
            .sameSite("Strict")
            .build()
    }
}

package com.service.admin.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.access-token-expiration}")
    private val validityAccessTime: Long,
    @Value("\${jwt.refresh-token-expiration}")
    private val validityRefreshTime: Long
){

    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    // Access/Refresh Token 생성
    fun createToken(flag:String, userId: String): String {
        val claims: Claims = Jwts.claims().setSubject(userId) // Token에 사용자 아이디 추가
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

    // Token에서 사용자 아이디 추출
    fun getUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    // Token 유효성 검사
    fun isTokenExpired(token: String): Boolean {
        val expiration = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .expiration

        return expiration.before(Date())
    }

    // Token 검증
    fun validateToken(token: String, userId: String): Boolean {
        return userId == getUsername(token) && !isTokenExpired(token)
    }
}

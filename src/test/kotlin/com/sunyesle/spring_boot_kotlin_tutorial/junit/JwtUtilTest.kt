package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.sunyesle.spring_boot_kotlin_tutorial.security.JwtUtil
import io.jsonwebtoken.Claims
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import javax.crypto.SecretKey
import kotlin.test.Test

class JwtUtilTest {

    private val secretKey: SecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)
    private val refreshSecretKey: SecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)
    private val base64Secret: String = Encoders.BASE64.encode(secretKey.encoded)
    private val base64RefreshSecret: String = Encoders.BASE64.encode(refreshSecretKey.encoded)

    private val jwtUtil = JwtUtil(base64Secret, base64RefreshSecret)

    @Test
    fun `generateAccessToken - 액세스 토큰을 생성한다`() {
        val userId = "testUser"
        val roles = listOf("ROLE_USER")
        val token = jwtUtil.generateAccessToken(userId, roles)

        val claims: Claims = jwtUtil.extractClaimsFromAccessToken(token)

        assertThat(claims.subject).isEqualTo(userId)
        val extractedRoles = claims[JwtUtil.CLAIM_KEY_ROLES] as List<*>
        assertThat(extractedRoles).isEqualTo(roles)
        assertThat(claims.issuedAt).isNotNull()
        assertThat(claims.expiration).isNotNull()
    }

    @Test
    fun `extractClaimsFromAccessToken - 유효하지 않은 액세스 토큰이 주어질 경우 예외가 발생한다`() {
        val invalidToken = "invalid token"

        assertThatThrownBy { jwtUtil.extractClaimsFromAccessToken(invalidToken) }
            .isInstanceOf(Exception::class.java)
    }
}

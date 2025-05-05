package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.sunyesle.spring_boot_kotlin_tutorial.security.JwtAuthenticationException
import com.sunyesle.spring_boot_kotlin_tutorial.security.JwtAuthenticationProvider
import com.sunyesle.spring_boot_kotlin_tutorial.security.JwtAuthenticationToken
import com.sunyesle.spring_boot_kotlin_tutorial.security.JwtUtil
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.crypto.SecretKey

class JwtAuthenticationProviderTest {

    private val secretKey: SecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)
    private val refreshSecretKey: SecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)
    private val base64Secret: String = Encoders.BASE64.encode(secretKey.encoded)
    private val base64RefreshSecret: String = Encoders.BASE64.encode(refreshSecretKey.encoded)
    private val jwtUtil = JwtUtil(base64Secret, base64RefreshSecret)

    private lateinit var userDetailsService: UserDetailsService
    private lateinit var provider: JwtAuthenticationProvider

    @BeforeEach
    fun setup() {
        userDetailsService = mockk()
        provider = JwtAuthenticationProvider(jwtUtil, userDetailsService)
    }

    @Test
    fun `authenticate - 엑세스 토큰이 유효하다면 JwtAuthenticationToken을 반환한다`() {
        val username = "testUser"
        val roles = listOf("ROLE_USER")
        val token = jwtUtil.generateAccessToken(username, roles)
        val authToken = JwtAuthenticationToken(token)
        val userDetails: UserDetails = User(username, "", roles.map { SimpleGrantedAuthority(it) })
        every { userDetailsService.loadUserByUsername(username) } returns userDetails

        val result = provider.authenticate(authToken) as JwtAuthenticationToken

        assertThat(result.principal).isEqualTo(userDetails)
        assertThat(result.credentials).isEqualTo("")
        assertThat(result.isAuthenticated).isTrue()
        val authorities = result.authorities.map { it.authority }
        assertThat(authorities).containsAll(roles)
    }

    @Test
    fun `authenticate - 토큰이 유효하지 않다면 JwtAuthenticationException이 발생한다`() {
        val invalidToken = "invalidToken"
        val authToken = JwtAuthenticationToken(invalidToken)

        assertThatThrownBy { provider.authenticate(authToken) }
            .isInstanceOf(JwtAuthenticationException::class.java)
    }

    @Test
    fun `supports - 지원하는 authentication일 경우 true를 반환한다`() {
        assertThat(provider.supports(JwtAuthenticationToken::class.java)).isTrue()
    }

    @Test
    fun `supports - 지원하지 않는 authentication일 경우 false를 반환한다`() {
        assertThat(provider.supports(UsernamePasswordAuthenticationToken::class.java)).isFalse()
        assertThat(provider.supports(AbstractAuthenticationToken::class.java)).isFalse()
        assertThat(provider.supports(Authentication::class.java)).isFalse()
    }
}

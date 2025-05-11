package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.common.AcceptanceTest
import com.sunyesle.spring_boot_kotlin_tutorial.security.RefreshTokenRequest
import com.sunyesle.spring_boot_kotlin_tutorial.security.TokenRequest
import io.kotest.core.spec.style.FunSpec
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.context.jdbc.Sql

@AcceptanceTest
@Sql(scripts = ["classpath:acceptance/user.sql"])
class AuthAcceptanceTest : FunSpec({

    context("토큰 발급") {

        test("유효한 정보를 입력하면 토큰을 반환한다") {
            // given
            val request = TokenRequest("johnDoe", "password")

            // when
            val response = generateToken(request)

            // then
            assertThat(response.statusCode).isEqualTo(200)
            assertThat(response.jsonPath().getString("accessToken")).isNotNull()
            assertThat(response.jsonPath().getString("refreshToken")).isNotNull()
        }

        test("유효하지 않은 정보를 입력하면 401 응답을 반환한다") {
            // given
            val invalidRequest = TokenRequest("invalidUsername", "invalidPassword")

            // when
            val response = generateToken(invalidRequest)

            // then
            assertThat(response.statusCode).isEqualTo(401)
        }
    }

    context("액세스 토큰 재발급") {

        test("유효한 리프레시 토큰을 입력하면 액세스 토큰을 반환한다") {
            // given
            val tokenRequest = TokenRequest("johnDoe", "password")
            val refreshToken = generateToken(tokenRequest).jsonPath().getString("refreshToken")
            val request = RefreshTokenRequest(refreshToken)

            // when
            val response = reissueAccessToken(request)

            // then
            assertThat(response.statusCode).isEqualTo(200)
            assertThat(response.jsonPath().getString("accessToken")).isNotNull()
        }

        test("유효하지 않은 리프레시 토큰을 입력하면 401 응답을 반환한다") {
            // given
            val invalidRequest = RefreshTokenRequest("invalidRefreshToken")

            // when
            val response = reissueAccessToken(invalidRequest)

            // then
            assertThat(response.statusCode).isEqualTo(401)
        }
    }

    context("USER 권한이 필요한 API") {

        test("USER 권한으로 접근할 수 있다") {
            val tokenRequest = TokenRequest("johnDoe", "password")
            val accessToken = generateToken(tokenRequest).jsonPath().getString("accessToken")

            Given {
                header("Authorization", "Bearer $accessToken")
                log().all()
            } When {
                get("/api/auth/test/user")
            } Then {
                statusCode(200)
                log().all()
            }
        }

        test("토큰 없이 접근하면 401 응답을 반환한다") {
            Given {
                log().all()
            } When {
                get("/api/auth/test/user")
            } Then {
                statusCode(401)
                log().all()
            }
        }
    }

    context("ADMIN 권한이 필요한 API") {

        test("USER 권한으로 접근하면 403 응답을 반환한다") {
            val userTokenRequest = TokenRequest("johnDoe", "password")
            val accessToken = generateToken(userTokenRequest).jsonPath().getString("accessToken")

            Given {
                header("Authorization", "Bearer $accessToken")
                log().all()
            } When {
                get("/api/auth/test/admin")
            } Then {
                statusCode(403)
                log().all()
            }
        }

        test("토큰 없이 접근하면 401 응답을 반환한다") {
            Given {
                log().all()
            } When {
                get("/api/auth/test/admin")
            } Then {
                statusCode(401)
                log().all()
            }
        }
    }
})

private fun generateToken(request: TokenRequest): Response {
    return Given {
        body(request)
        log().all()
    } When {
        post("/api/auth/token")
    } Then {
        log().all()
    } Extract {
        response()
    }
}

private fun reissueAccessToken(invalidRequest: RefreshTokenRequest): Response {
    val response = Given {
        body(invalidRequest)
        log().all()
    } When {
        post("/api/auth/token/refresh")
    } Then {
        log().all()
    } Extract {
        response()
    }
    return response
}

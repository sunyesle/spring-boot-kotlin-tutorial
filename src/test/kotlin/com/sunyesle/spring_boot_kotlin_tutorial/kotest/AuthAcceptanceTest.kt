package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.common.AcceptanceTest
import com.sunyesle.spring_boot_kotlin_tutorial.security.RefreshTokenRequest
import com.sunyesle.spring_boot_kotlin_tutorial.security.TokenRequest
import com.sunyesle.spring_boot_kotlin_tutorial.user.Role
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.springframework.security.crypto.password.PasswordEncoder

@AcceptanceTest
class AuthAcceptanceTest(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) : DescribeSpec({

    beforeSpec {
        val user = User(
            username = "johnDoe",
            password = passwordEncoder.encode("password"),
            firstname = "John",
            lastname = "Doe",
            role = Role.USER
        )
        userRepository.save(user)
    }

    describe("인증 API") {

        describe("토큰 발급") {

            context("유효한 정보를 입력하면") {
                val request = TokenRequest("johnDoe", "password")
                val response = generateToken(request)

                it("토큰을 반환한다") {
                    assertThat(response.statusCode).isEqualTo(200)
                    assertThat(response.jsonPath().getString("accessToken")).isNotNull()
                    assertThat(response.jsonPath().getString("refreshToken")).isNotNull()
                }
            }

            context("유효하지 않은 정보를 입력하면") {
                val invalidRequest = TokenRequest("null", "null")
                val response = generateToken(invalidRequest)

                it("401 응답을 반환한다") {
                    assertThat(response.statusCode).isEqualTo(401)
                }
            }
        }

        describe("액세스 토큰 재발급") {

            context("유효한 리프레시 토큰을 입력하면") {
                val tokenRequest = TokenRequest("johnDoe", "password")
                val refreshToken = generateToken(tokenRequest).jsonPath().getString("refreshToken")
                val request = RefreshTokenRequest(refreshToken)

                val response = reissueAccessToken(request)

                it("액세스 토큰을 반환한다") {
                    assertThat(response.statusCode).isEqualTo(200)
                    assertThat(response.jsonPath().getString("accessToken")).isNotNull()
                }
            }

            context("유효하지 않은 리프레시 토큰을 입력하면") {
                val invalidRequest = RefreshTokenRequest("null")

                val response = reissueAccessToken(invalidRequest)

                it("401 응답을 반환한다") {
                    assertThat(response.statusCode).isEqualTo(401)
                }
            }
        }

        describe("USER 권한이 필요한 API") {

            it("USER 권한으로 접근할 수 있다") {
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

            it("토큰 없이 접근하면 401 응답을 반환한다") {
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

        describe("ADMIN 권한이 필요한 API") {

            it("USER 권한으로 접근하면 403 응답을 반환한다") {
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

            it("토큰 없이 접근하면 401 응답을 반환한다") {
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

package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.security.TokenRequest
import com.sunyesle.spring_boot_kotlin_tutorial.user.Role
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserSaveRequest
import io.restassured.RestAssured.requestSpecification
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class AuthAcceptanceTest : AcceptanceSpecs({

    describe("인증 API") {

        describe("토큰 발급") {

            context("유효한 정보를 입력하면") {
                val request = TokenRequest("johnDoe", "password")
                val response = generateToken(request)

                it("토큰을 반환한다") {
                    Assertions.assertThat(response.statusCode).isEqualTo(200)
                    Assertions.assertThat(response.jsonPath().getString("accessToken")).isNotNull()
                }
            }

            context("유효하지 않은 정보를 입력하면") {
                val invalidRequest = TokenRequest("null", "null")
                val response = generateToken(invalidRequest)

                it("401 응답을 반환한다") {
                    Assertions.assertThat(response.statusCode).isEqualTo(401)
                }
            }
        }

    }
}) {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    init {
        beforeSpec {
            databaseCleanup.execute()

            val user = User(
                username = "johnDoe",
                password = passwordEncoder.encode("password"),
                firstname = "John",
                lastname = "Doe",
                role = Role.USER
            )
            userRepository.save(user)
        }
    }
}

private fun generateToken(request: TokenRequest): Response {
    return Given {
        spec(requestSpecification)
        body(request)
    } When {
        post("/api/auth/token")
    } Extract {
        response()
    }
}
package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.common.AcceptanceTest
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserSaveRequest
import io.kotest.core.spec.style.DescribeSpec
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.Assertions

@AcceptanceTest
class UserAcceptanceTest : DescribeSpec({

    describe("사용자 API") {
        val request = UserSaveRequest("johnDoe", "password", "John", "Doe")

        describe("사용자 저장") {

            context("유효한 정보를 입력하면") {
                val response = saveUser(request)

                it("201 응답을 반환한다") {
                    Assertions.assertThat(response.statusCode).isEqualTo(201)
                    Assertions.assertThat(response.jsonPath().getLong("id")).isNotNull()
                }
            }

            context("유효하지 않은 정보를 입력하면") {
                val invalidRequest = UserSaveRequest("", "", "", "")
                val response = saveUser(invalidRequest)

                it("400 응답을 반환한다") {
                    Assertions.assertThat(response.statusCode).isEqualTo(400)
                    val errors = response.jsonPath().getList("data", Map::class.java)
                    Assertions.assertThat(errors).isNotEmpty()
                }
            }
        }

        context("사용자 전체 목록을 조회하면") {
            val response = findUsers()

            it("목록에 해당 사용자가 존재한다") {
                val users = response.jsonPath().getList("", Map::class.java)
                Assertions.assertThat(users).hasSize(1)
                Assertions.assertThat(users[0]["username"]).isEqualTo("johnDoe")
            }
        }

        context("로그인으로 사용자를 조회하면") {
            val response = findUserByUsername(request.username)

            it("해당 사용자 정보를 반환한다") {
                val json = response.jsonPath()
                Assertions.assertThat(json.getString("firstname")).isEqualTo("John")
                Assertions.assertThat(json.getString("lastname")).isEqualTo("Doe")
            }
        }
    }
})

private fun saveUser(request: UserSaveRequest): Response {
    return Given {
        body(request)
        log().all()
    } When {
        post("/api/user")
    } Then {
        log().all()
    } Extract {
        response()
    }
}

private fun findUsers(): Response {
    return Given {
        log().all()
    } When {
        get("/api/user")
    } Then {
        log().all()
    } Extract {
        response()
    }
}

private fun findUserByUsername(username: String): Response {
    return Given {
        log().all()
    } When {
        get("/api/user/{username}", username)
    } Then {
        log().all()
    } Extract {
        response()
    }
}

package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.user.UserSaveRequest
import io.restassured.RestAssured.requestSpecification
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.Assertions

class UserAcceptanceTest : AcceptanceSpecs({

    describe("사용자 API") {
        val request = UserSaveRequest("johnDoe", "John", "Doe")

        context("사용자를 저장하면") {
            val response = saveUser(request)

            it("201 응답을 반환한다") {
                Assertions.assertThat(response.statusCode).isEqualTo(201)
                Assertions.assertThat(response.jsonPath().getLong("id")).isNotNull()
            }
        }

        context("사용자 전체 목록을 조회하면") {
            val response = findUsers()

            it("목록에 해당 사용자가 존재한다") {
                val users = response.jsonPath().getList("", Map::class.java)
                Assertions.assertThat(users).hasSize(1)
                Assertions.assertThat(users[0]["login"]).isEqualTo("johnDoe")
            }
        }

        context("로그인으로 사용자를 조회하면") {
            val response = findUserByLogin(request.login)

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
        spec(requestSpecification)
        body(request)
    } When {
        post("/api/user")
    } Extract {
        response()
    }
}

private fun findUsers(): Response {
    return Given {
        spec(requestSpecification)
    } When {
        get("/api/user")
    } Extract {
        response()
    }
}

private fun findUserByLogin(login: String): Response {
    return Given {
        spec(requestSpecification)
    } When {
        get("/api/user/{login}", login)
    } Extract {
        response()
    }
}

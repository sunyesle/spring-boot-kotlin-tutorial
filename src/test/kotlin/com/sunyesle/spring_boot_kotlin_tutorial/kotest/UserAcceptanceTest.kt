package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.common.AcceptanceTest
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserSaveRequest
import io.kotest.core.spec.style.FunSpec
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.springframework.test.context.jdbc.Sql

@AcceptanceTest
@Sql(scripts = ["classpath:acceptance/user.sql"])
class UserAcceptanceTest : FunSpec({

    context("사용자 저장") {

        test("유효한 정보를 입력하면 201 응답을 반환한다") {
            // given
            val request = UserSaveRequest("johnDoe", "password", "John", "Doe")

            // when
            val response = saveUser(request)

            // then
            Assertions.assertThat(response.statusCode).isEqualTo(201)
            Assertions.assertThat(response.jsonPath().getLong("id")).isNotNull()
        }

        test("유효하지 않은 정보를 입력하면 400 응답을 반환한다") {
            // given
            val invalidRequest = UserSaveRequest("", "", "", "")

            // when
            val response = saveUser(invalidRequest)

            // then
            Assertions.assertThat(response.statusCode).isEqualTo(400)
            val errors = response.jsonPath().getList("data", Map::class.java)
            Assertions.assertThat(errors).isNotEmpty()
        }
    }

    context("사용자 목록 조회") {

        test("사용자 목록을 반환한다") {
            Given {
                log().all()
            } When {
                get("/api/user")
            } Then {
                body("", hasSize<Any>(1))
                body("[0].username", equalTo("johnDoe"))
                log().all()
            }
        }
    }

    context("사용자 단건 조회") {
        test("username으로 사용자를 조회하면 해당 사용자 정보를 반환한다") {
            Given {
                log().all()
            } When {
                get("/api/user/{username}", "johnDoe")
            } Then {
                body("username", equalTo("johnDoe"))
                log().all()
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

package com.sunyesle.spring_boot_kotlin_tutorial

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.Test

class AcceptanceTests : BaseAcceptanceTest() {

    private val userSaveRequest = UserSaveRequest(
        login = "johnDoe",
        firstname = "John",
        lastname = "Doe"
    )

    @Test
    fun `user test`() {
        `save user`(userSaveRequest)

        `find users`()

        `find user with given login`(userSaveRequest.login)
    }

    fun `save user`(request: UserSaveRequest) {
        Given {
            spec(requestSpecification)
            body(request)
        } When {
            post("/api/user")
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            body("id", IsNull.notNullValue())
        }
    }

    fun `find users`() {
        Given {
            spec(requestSpecification)
        } When {
            get("/api/user")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", IsEqual.equalTo(1))
        }
    }

    fun `find user with given login`(login: String) {
        Given {
            spec(requestSpecification)
        } When {
            get("/api/user/{login}", login)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(
                "firstname", IsEqual.equalTo("John"),
                "lastname", CoreMatchers.equalTo("Doe")
            )
        }
    }
}
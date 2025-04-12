package com.sunyesle.spring_boot_kotlin_tutorial

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Test

class AcceptanceTests : BaseAcceptanceTest() {

    @Test
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

    @Test
    fun `find user with given login`() {
        Given {
            spec(requestSpecification)
        } When {
            get("/api/user/{login}", "johnDoe")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(
                "firstname", IsEqual.equalTo("John"),
                "lastname", CoreMatchers.equalTo("Doe")
            )
        }
    }
}
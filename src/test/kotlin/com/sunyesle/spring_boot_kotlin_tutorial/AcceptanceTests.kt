package com.sunyesle.spring_boot_kotlin_tutorial

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.BeforeAll

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTests {

    lateinit var requestSpecification: RequestSpecification

    @LocalServerPort
    var port: Int = 0

    @BeforeAll
    fun setUp() {
        RestAssured.port = port

        val logConfig = LogConfig.logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)

        val config = RestAssured.config().logConfig(logConfig)

        requestSpecification = RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setConfig(config)
            .build()
    }

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
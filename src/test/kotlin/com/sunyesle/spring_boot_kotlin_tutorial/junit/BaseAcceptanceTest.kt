package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.sunyesle.spring_boot_kotlin_tutorial.common.DatabaseCleanup
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseAcceptanceTest {

    protected lateinit var requestSpecification: RequestSpecification

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var databaseCleanup: DatabaseCleanup

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

        databaseCleanup.execute()
    }
}
package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.common.DatabaseCleanup
import io.kotest.core.spec.style.DescribeSpec
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AcceptanceSpecs (body: DescribeSpec.() -> Unit) : DescribeSpec(body) {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var databaseCleanup: DatabaseCleanup

    init {
        beforeSpec {
            RestAssured.reset()

            RestAssured.port = port

            val logConfig = LogConfig.logConfig()
                .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)

            val config = RestAssured.config().logConfig(logConfig)

            RestAssured.requestSpecification = RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setConfig(config)
                .addFilter(RequestLoggingFilter())
                .addFilter(ResponseLoggingFilter())
                .build()

            databaseCleanup.execute()
        }
    }
}

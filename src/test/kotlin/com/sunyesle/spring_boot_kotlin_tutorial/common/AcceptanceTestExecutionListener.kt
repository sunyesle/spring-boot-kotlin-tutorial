package com.sunyesle.spring_boot_kotlin_tutorial.common

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class AcceptanceTestExecutionListener : AbstractTestExecutionListener() {
    override fun beforeTestClass(testContext: TestContext) {
        // RestAssured 설정
        val serverPort = testContext.applicationContext.environment
            .getProperty("local.server.port", Int::class.java)
            ?: throw IllegalStateException("localServerPort cannot be null")

        RestAssured.port = serverPort

        val logConfig = LogConfig.logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)

        val config = RestAssured.config().logConfig(logConfig)

        RestAssured.requestSpecification = RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setConfig(config)
            .build()

        // DB 데이터 초기화
        val databaseCleanup = testContext.applicationContext.getBean(DatabaseCleanup::class.java)

        databaseCleanup.execute()
    }
}

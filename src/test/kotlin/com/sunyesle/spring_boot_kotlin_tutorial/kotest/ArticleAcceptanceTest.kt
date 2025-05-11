package com.sunyesle.spring_boot_kotlin_tutorial.kotest

import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleRequest
import com.sunyesle.spring_boot_kotlin_tutorial.common.AcceptanceTest
import com.sunyesle.spring_boot_kotlin_tutorial.security.TokenRequest
import io.kotest.core.spec.style.FunSpec
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.*
import org.springframework.test.context.jdbc.Sql

@AcceptanceTest
@Sql(scripts = ["classpath:acceptance/article.sql"])
class ArticleAcceptanceTest : FunSpec({

    context("글 저장") {

        test("유효한 정보를 입력하면 201 응답을 반환한다") {
            // given
            val accessToken = generateToken(TokenRequest("johnDoe", "password"))
            val request = ArticleRequest("new article", "test headline", "test content")

            // when
            Given{
                header("Authorization", "Bearer $accessToken")
                body(request)
                log().all()
            } When {
                post("/api/article")
            } Then {
                statusCode(201)
                body("id", notNullValue())
                log().all()
            }
        }
    }

    context("글 목록 조회") {

        test("글 목록을 반환한다") {
            Given {
                log().all()
            } When {
                get("/api/article")
            } Then {
                body("", hasSize<Any>(2))
                log().all()
            }
        }
    }

    context("글 단건 조회") {
        test("slug로 글를 조회하면 해당 글 정보를 반환한다") {
            Given {
                log().all()
            } When {
                get("/api/article/{slug}", "spring-boot-guide")
            } Then {
                body("title", equalTo("Spring Boot Guide"))
                log().all()
            }
        }
    }
})

private fun generateToken(request: TokenRequest): String {
    return Given {
        body(request)
    } When {
        post("/api/auth/token")
    } Extract {
        path("accessToken")
    }
}

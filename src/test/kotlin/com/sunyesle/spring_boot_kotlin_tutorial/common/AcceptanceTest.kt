package com.sunyesle.spring_boot_kotlin_tutorial.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners

@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
    value = [AcceptanceTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
annotation class AcceptanceTest()

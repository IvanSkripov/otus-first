package ru.otus.kotlin.course.app.spring.repo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.kotlin.course.app.spring.AppWsBase

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppSpringRepoTest: AppWsBase() {

    @LocalServerPort
    var port: Int = 0
    override fun port(): Int = port

    @Test
    fun imageProcessTest() {

    }
}
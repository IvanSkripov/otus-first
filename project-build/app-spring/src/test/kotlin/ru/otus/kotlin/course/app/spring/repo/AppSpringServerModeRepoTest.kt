package ru.otus.kotlin.course.app.spring.repo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import ru.otus.kotlin.course.app.spring.config.PsConfig
import ru.otus.kotlin.course.app.spring.controllers.PsContollerWS
import ru.otus.kotlin.course.app.spring.controllers.PsController


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PsController::class, PsConfig::class, PsContollerWS::class)
class AppSpringServerModeRepoTest: AppRepoBase() {

    @LocalServerPort
    var port: Int = 0
    override fun port(): Int = port

    @Test
    public fun imageProcessServerTest() {
        imageProcessTest(DBG_PROD)
    }
}
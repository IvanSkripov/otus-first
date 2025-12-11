package ru.otus.kotlin.course.app.spring.repo

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import ru.otus.kotlin.course.app.spring.config.PsConfig
import ru.otus.kotlin.course.app.spring.controllers.PsContollerWS
import ru.otus.kotlin.course.app.spring.controllers.PsController
import java.sql.DriverManager


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PsController::class, PsConfig::class, PsContollerWS::class)
class AppSpringTestModeRepoTest: AppRepoBase() {

    @LocalServerPort
    var port: Int = 0
    override fun port(): Int = port

    @Test
    public fun imageProcessTestModeTest() {
        imageProcessTest(DBG_TEST)
    }
}
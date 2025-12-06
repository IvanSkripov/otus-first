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
class AppSpringProdModeRepoTest: AppRepoBase() {

    @LocalServerPort
    var port: Int = 0
    override fun port(): Int = port

    @Test
    public fun imageProcessProdTest() {
        imageProcessTest(DBG_PROD)
    }

    companion object {
        //@JvmStatic
        val pg = PostgreSQLContainer(
            DockerImageName.parse("postgres:15.4")
                .asCompatibleSubstituteFor("postgres"))

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            pg.start()
            val changelog = "db/data-set-v0.yml"
            println("Migration starting. ChangeLog: [${changelog}] on DB url: ${pg.getJdbcUrl()}, user: ${pg.getUsername()}, password: ${pg.getPassword()}")
            val conn = DriverManager.getConnection(pg.getJdbcUrl(), pg.getUsername(), pg.getPassword())
            val database = liquibase.database.DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(JdbcConnection(conn))
            val resourceAccessor = ClassLoaderResourceAccessor(this::class.java.classLoader)
            val lb = Liquibase(changelog, resourceAccessor, database)
            lb.update("")

        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            pg.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        public fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("psql.url", pg::getJdbcUrl);
            registry.add("psql.user", pg::getUsername);
            registry.add("psql.password", pg::getPassword);
           }
        }



}
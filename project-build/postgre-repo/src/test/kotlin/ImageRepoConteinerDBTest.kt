package ru.otus.kotlin.course.common.stubs.repo.postgre.test

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.Test
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import repo.SQLParams
import java.sql.DriverManager
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse


class ImageRepoConteinerDBTest(
) {

    @Test
    fun executeTests() {

        // run test only on Linux
        val osName = System.getProperty("os.name")
        println("Check OS: ${osName}. Start tests only on Linux")
        if (! osName.contains("Linux")) { return  }

        println("Creating Container")
        val pg = PostgreSQLContainer(
            DockerImageName.parse("postgres:15.4")
                .asCompatibleSubstituteFor("postgres")
        )
        pg.start()
        println("Created Container ${pg.isCreated}")
        val params = SQLParams(
            url = pg.jdbcUrl,
            user = pg.username,
            password = pg.password
        )
        println("SQLParams = ${params}")
        try {
            runMigration(params)

            // run tests
            val runner: TestRunner = TestRunner(params)

            println("Test: create")
            runner.createImageTest()
            println("Test: read")
            runner.readImageTest()
            println("Test: update")
            runner.updateImageTest()
            println("Test: delete")
            runner.deleteImageTest()
            println("Test: search")
            runner.searchImagesTest()
        } catch (e:Throwable) {
            println("Catch error e: ${e}")
            println("StackTrace: ${e.printStackTrace()}")
            assertFalse(true, "Tests Failed")
        } finally {
            println("Stoped Container")
            pg.stop()
        }


    }

    private fun runMigration(params: SQLParams) {

        //val changelog = "classpath:resources/db/data-set-v0.yml"
        val changelog = "db/data-set-v0.yml"

        println("Migration starting. ChangeLog: [${changelog}], params: [${params}]")
        val conn = DriverManager.getConnection(params.url, params.user, params.password)
        val database = liquibase.database.DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(JdbcConnection(conn))
        val resourceAccessor = ClassLoaderResourceAccessor(this::class.java.classLoader)
        val lb = Liquibase(changelog, resourceAccessor, database)
        println("Before update")
        lb.update("")
        println("Migration finished")

    }

}

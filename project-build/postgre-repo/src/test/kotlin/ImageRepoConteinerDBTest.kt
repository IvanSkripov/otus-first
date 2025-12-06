package ru.otus.kotlin.course.common.stubs.repo.postgre.test

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.Test
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import repo.SQLParams
import java.sql.DriverManager

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

            runner.createImageTest()
            runner.readImageTest()
            runner.updateImageTest()
            runner.deleteImageTest()
            runner.searchImagesTest()
        } catch (e:Throwable) {
            println("Catch error e: ${e}")
        } finally {
            println("Stoped Container")
            pg.stop()
        }


    }

    private fun runMigration(params: SQLParams) {

        val changelog = "/project-build/postgre-repo/src/db/data-set-v0.yml"
        println("Migration starting. ChangeLog: [${changelog}], params: [${params}]")
        DriverManager.getConnection(params.url, params.user, params.password).use { conn ->
            {
                val database = liquibase.database.DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(JdbcConnection(conn))
                val resourceAccessor = ClassLoaderResourceAccessor(this::class.java.classLoader)
                Liquibase(changelog, resourceAccessor, database).use { lb ->
                    println("Before update")
                    lb.update("")
                    println("Migration finished")
                }
            }
        }

    }
}
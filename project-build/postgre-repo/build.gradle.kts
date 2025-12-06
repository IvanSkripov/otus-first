import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

plugins {
    id("build-jvm")
    alias(libs.plugins.studer)
    alias(libs.plugins.liquibase)
}

buildscript {
    dependencies {
        classpath(libs.liquibase)
        classpath(libs.test.containers)
        classpath(libs.test.containers.postgre)
    }
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation(libs.uuid)
    // jooq
    jooqGenerator(libs.postgresql)
    // liquibase
    liquibaseRuntime ("info.picocli:picocli:4.6.3")
    liquibaseRuntime("org.liquibase:liquibase-core:4.31.1")
    liquibaseRuntime(libs.postgresql)
    // postgre
    implementation(libs.postgresql)
    implementation(libs.test.containers)

    implementation(projects.contextCommon)
    implementation(projects.contextStubs)

    testImplementation(kotlin("test"))
    testImplementation(libs.test.coroutines)

    testImplementation(libs.test.containers.container.postgre)
    testImplementation(libs.liquibase)

}

liquibase {
    activities {
        register("main") {
            this.arguments = mapOf(
                "changelogFile" to
                        "/project-build/postgre-repo/src/main/resources/db/data-set-v0.yml",
                        //"${layout.projectDirectory.dir("./src/db")}/data-set-v0.yml",
                "url" to "jdbc:postgresql://localhost:5432/postgres",
                "username" to "postgres",
                "password" to "mysecretpassword",
                "logLevel" to "debug" //если хотим видить логи при выполнение команд

            )
        }
        runList = "main"
    }
}

val jooqVersion = "3.19.8"

val postgresDelegate = lazy {
    PostgreSQLContainer<Nothing>(
        DockerImageName.parse("postgres:15.4").asCompatibleSubstituteFor("postgres")
    ).also { it.start() }
}

val postgres: PostgreSQLContainer<Nothing> by postgresDelegate



// use --debug in gradle properites
tasks.named<org.liquibase.gradle.LiquibaseTask>("update") {
    onlyIf { project.findProperty("jooq.enabled") == "true" }
    doFirst {
        val args = liquibase.activities.get("main").arguments as HashMap<String, String>
//        args.put("url", postgres.jdbcUrl)
//        args.put("user", postgres.username)
//        args.put("password", postgres.password)
        println ("LiquibaseTask ========== ${liquibase.activities.get("main").arguments}")
    }
}


// Конфиг jOOQ.
// Мы будем переопределять подключение динамически в задаче generateJooq
val jooqTargetDir = "${layout.projectDirectory.dir("./src/main/kotlin/jooq")}"
//val jooqTargetDir = "${layout.buildDirectory.get()}/generated-sources/jooq"

jooq {
    version.set(jooqVersion)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        schemata.add(
                            org.jooq.meta.jaxb.SchemaMappingType().withInputSchema("public")
                        )
                        excludes = """
                            databasechangelog.*|
                            flyway_schema_history|
                            pg_.*|
                            information_schema\..*
                        """.trimIndent()
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    generate.apply {
                        isPojos = true
                        isRecords = true
                        isFluentSetters = true
                        isKotlinNotNullPojoAttributes = true
                    }
                    target.apply {
                        packageName = "ru.otus.kotlin.course.repo.postgre"
                        directory = jooqTargetDir
                        encoding = "UTF-8"
                    }
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    onlyIf { project.findProperty("jooq.enabled") == "true" }

    dependsOn(tasks.named("update"))

    val jooq = this

    doFirst {
        val fieldName = "jooqConfiguration"
        val field = nu.studer.gradle.jooq.JooqGenerate::class.memberProperties.find { it.name == fieldName }
        field?.let {
            field.isAccessible = true
            val configuration = field.get(jooq) as org.jooq.meta.jaxb.Configuration
//            configuration.jdbc.url = postgres.jdbcUrl
//            configuration.jdbc.user = postgres.username
//            configuration.jdbc.password = postgres.password
//
            configuration.jdbc.url = "jdbc:postgresql://localhost:5432/postgres"
            configuration.jdbc.user = "postgres"
            configuration.jdbc.password = "mysecretpassword"
        }
    }

    finalizedBy("stopPostgreSQLContainer")
}


// Task Stop Container
tasks.register("stopPostgreSQLContainer") {

    onlyIf {
        postgresDelegate.isInitialized()
    }
    doLast {
        postgres.stop()
        postgres.close()
    }
}

tasks.test {
    testLogging {
        showStandardStreams = true
        events ("passed", "skipped", "failed", "standardOut", "standardError")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}




// Чтобы IDE видела сгенерированный код
configure<SourceSetContainer> {
    named("main") {
        java.srcDir(jooqTargetDir)
    }
}
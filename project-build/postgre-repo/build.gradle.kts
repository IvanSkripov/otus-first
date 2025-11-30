import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

//import kotlin.reflect.full.memberProperties
//import kotlin.reflect.jvm.isAccessible
//import org.jooq.meta.jaxb.Configuration as JooqConfiguration
//import org.gradle.api.tasks.Input
//import org.gradle.api.tasks.TaskAction
//import org.jooq.meta.jaxb.*

plugins {
    id("build-jvm")
    //alias(libs.plugins.jooq.generator)
    alias(libs.plugins.studer)
    alias(libs.plugins.liquibase)
}

buildscript {
    dependencies {
        classpath("org.liquibase:liquibase-core:4.31.1")
        classpath("org.testcontainers:postgresql:1.18.1")
    }
}

dependencies {

    implementation(kotlin("stdlib"))
    // jooq
    jooqGenerator(libs.postgresql)
    // liquibase
    liquibaseRuntime ("info.picocli:picocli:4.6.3")
    liquibaseRuntime("org.liquibase:liquibase-core:4.31.1")
    liquibaseRuntime(libs.postgresql)
    // postgre
    implementation(libs.postgresql)
}

liquibase {
    activities {
        register("main") {
            this.arguments = mapOf(
                "changelogFile" to
                        "${layout.projectDirectory.dir("./src/db")}/data-set-v0.yml",
//                "url" to "jdbc:postgresql://localhost:5432/testDb",
//                "username" to "test",
//                "password" to "test",
                "logLevel" to "debug" //если хотим видить логи при выполнение команд
            )
        }
        runList = "main"
    }
}

val jooqVersion = "3.19.8"

//val postgres: PostgreSQLContainer<Nothing> by postgresDelegate

//tasks.named<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrate") {
//    doFirst {
//        url = postgres.jdbcUrl
//        user = postgres.username
//        password = postgres.password
//        defaultSchema = "good_food"
//        locations = arrayOf("filesystem:../src/main/resources/db/migration")
//    }
//    finalizedBy("stopPostgreSQLContainer")
//}

//// Uncomment to watch activity arguments
//// use --debug in gradle properites
//tasks.named<org.liquibase.gradle.LiquibaseTask>("validate") {
//    doFirst {
//        println ("LiquibaseTask ========== ${liquibase.activities.get("main").arguments}")
//    }
//}



// Конфиг jOOQ.
// Мы будем переопределять подключение динамически в задаче generateJooq
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
                            org.jooq.meta.jaxb.SchemaMappingType().withInputSchema("good_food")
                        )
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    generate.apply {
                        isPojos = true
                        isRecords = true
                        isFluentSetters = true
                        isKotlinNotNullPojoAttributes = true
                    }
                    target.apply {
                        packageName = "dev.test.jooq.goodfood"
                        directory = "${layout.buildDirectory.get()}/generated-sources/jooq"
                        encoding = "UTF-8"
                    }
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    onlyIf { project.findProperty("jooq.enabled") == "true" }

    //dependsOn(tasks.named("flywayMigrate"))
    //dependsOn(tasks.named("li"))

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
        }
    }

    finalizedBy("stopPostgreSQLContainer")
}

// Task Start Container
tasks.register("startPostgreSQLContainer") {
//    val postgresDelegate = lazy {
//        PostgreSQLContainer<Nothing>(
//            DockerImageName.parse("postgres:15.4").asCompatibleSubstituteFor("postgres")
//        ).also { it.start() }
//    }



    doFirst {
        println("startPostgreSQLContainer ----- ")
    }

//    doLast {
//        postgres.stop()
//        postgres.close()
//    }

}


// Task Stop Container
tasks.register("stopPostgreSQLContainer") {
    doFirst {
        println("stopPostgreSQLContainer ----- ")
    }

//    onlyIf {
//        postgresDelegate.isInitialized()
//    }
//    doLast {
//        postgres.stop()
//        postgres.close()
//    }
}



//// Задачи
//val startPostgres = tasks.register("startPostgres", StartPostgres::class.java)
//
//val liquibaseMigrate = tasks.register("liquibaseMigrate", LiquibaseMigrate::class.java) {
//    dependsOn(startPostgres)
//    changelogFile = liquibaseChangelogFile
//}


//
//tasks.named("generateJooq").configure {
//    dependsOn(liquibaseMigrate)
//    finalizedBy("stopPostgres")
//
//    doFirst {
//        // Подставим параметры подключения контейнера в конфигурацию jOOQ
//        val extra = project.extensions.extraProperties
//        val url = extra.get("pg_jdbc_url") as String
//        val user = extra.get("pg_username") as String
//        val pass = extra.get("pg_password") as String
//
//        project.logger.lifecycle("Configuring jOOQ to read from: $url")
//
//        jooq.configurations["main"]!!.jooqConfiguration.apply {
//            jdbc = Jdbc()
//                .withDriver("org.postgresql.Driver")
//                .withUrl(url)
//                .withUser(user)
//                .withPassword(pass)
//        }
//    }
//}
//



// Чтобы IDE видела сгенерированный код
//sourceSets {
//    val main by getting {
//        java.srcDir(jooqTargetDir)
//    }
//}



//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    kotlinOptions.jvmTarget = "17"
//}

//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(17))
//    }
//}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    dependsOn("generateJooq")
//}

//tasks {
//    compileKotlin {
//        dependsOn(jooqCodegen)
//    }
//}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("${layout.buildDirectory.get()}/generated-sources/jooq")
    }
}
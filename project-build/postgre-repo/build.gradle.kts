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
        classpath("org.testcontainers:testcontainers:2.0.2")
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
    implementation("org.testcontainers:testcontainers:2.0.2")

    implementation(projects.contextCommon)
    implementation(projects.contextStubs)

    testImplementation(kotlin("test"))
    testImplementation(libs.test.coroutines)
    testImplementation ("org.testcontainers:testcontainers-postgresql:2.0.2")

}

liquibase {
    activities {
        register("main") {
            this.arguments = mapOf(
                "changelogFile" to
                        "/project-build/postgre-repo/src/db/data-set-v0.yml",
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
val jooqTargetDir = "${layout.projectDirectory.dir("./src/main/kotlin")}"

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
                        //directory = "${layout.buildDirectory.get()}/generated-sources/jooq"
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

// Чтобы IDE видела сгенерированный код
sourceSets {
    val main by getting {
        java.srcDir(jooqTargetDir)
    }
}


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
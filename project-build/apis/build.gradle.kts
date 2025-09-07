plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir(
            "generate-resources/main/src/main/kotlin"
        ))
    }
}

openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1"
    generatorName = "kotlin"
    packageName = openapiGroup
    apiPackage = "${openapiGroup}.api"
    modelPackage = "${openapiGroup}.models"
    invokerPackage = "${openapiGroup}.invoker"
    inputSpec = rootProject.ext["api-spec"] as String

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
        put("modelTests", "true")
    }

    configOptions.set (
        mapOf (
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)
    testImplementation(kotlin("test-junit"))
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
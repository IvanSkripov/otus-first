plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir(
            "generater-resources/main/src/main/kotlinn"
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

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
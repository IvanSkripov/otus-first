plugins {
    id("build-jvm")
    alias(libs.plugins.jooq.generator)
}
dependencies {

    // Code generation specific dependencies, like JDBC drivers, codegen extensions, etc.
    jooqCodegen("...")
}

jooq {
    configuration {
        // ...
    }
}
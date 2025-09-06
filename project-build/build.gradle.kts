plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.otus.kotlin.course"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    // set("api-spec", specDir.file("open-api-chat-gpt.yml").toString())
    //set("api-spec", specDir.file("requestBody-test.yaml").toString())
    set("api-spec", specDir.file("open-api-images.yaml").toString())

}
plugins {
    kotlin("jvm") apply false
}

group = "ru.otus.kotlin.course"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}
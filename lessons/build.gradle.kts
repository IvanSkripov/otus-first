plugins {
    kotlin("jvm") apply false
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
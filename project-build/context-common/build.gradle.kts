plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.klogging)
    testImplementation(kotlin("test-junit"))
}
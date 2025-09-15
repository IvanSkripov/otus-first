plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.apis)
    implementation(projects.contextCommon)
    testImplementation(kotlin("test-junit"))
}
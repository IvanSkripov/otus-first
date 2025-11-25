plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(projects.apis)
    implementation(projects.contextCommon)
    testImplementation(kotlin("test-junit"))
}
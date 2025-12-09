plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.apis)
    implementation(projects.contextCommon)
    implementation(projects.libCor)
    testImplementation(kotlin("test-junit"))
}
plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.apis)
    implementation(projects.contextCommon)
    implementation(projects.contextStubs)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
package ru.otus.kotlin.course.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories

@Suppress("unused")
class BuildPluginJvm : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        group = rootProject.group
        version = rootProject.version
        repositories {
            mavenCentral()
        }
    }
}
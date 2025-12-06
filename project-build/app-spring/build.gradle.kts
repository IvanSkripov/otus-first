plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependencies)
	alias(libs.plugins.kotlin.sping)
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(libs.spring.webflux)
	implementation(libs.spring.webflux.ui)
	// implementation(libs.spring.websocket)
	// implementation(libs.spring.kafka)
	implementation(libs.spring.actuator)
	implementation(libs.coroutines.core)
	implementation(libs.coroutines.reactor)
	implementation(libs.coroutines.reactive)
	implementation(libs.coroutines.reactor.extentions)
	implementation(libs.kotlin.reflect)
	implementation(libs.klogging)
	implementation(libs.uuid)

 	// Subprojects

	implementation(projects.apis)
	implementation(projects.contextCommon)
	implementation(projects.contextStubs)
	implementation(projects.contextMappers)
	implementation(projects.postgreRepo)

	// Tests
	testImplementation(kotlin("test"))
	testImplementation(libs.test.spring.boot)
	testImplementation(libs.test.spring.kafka)
	testImplementation(libs.test.containers)
	testImplementation(libs.test.containers.postgre)
	testImplementation(libs.liquibase)
//	testImplementation("io.projectreactor:reactor-test")
//	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")


	//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks {
	withType<ProcessResources> {
		val files = listOf("api-spec").map {
			rootProject.ext[it]
		}
		println(files)
		from(files) {
			into("/static")
			filter {
				// Устанавливаем версию в сваггере
				it.replace("\${VERSION_APP}", project.version.toString())
			}

		}
	}
}


tasks.test {
	useJUnitPlatform()
	if (project.findProperty("use.db.container") != "true") {
		exclude("**/AppSpringProdModeRepoTest.class")
	}
	if (project.findProperty("use.db.server") != "true") {
		exclude("**/AppSpringServerModeRepoTest.class")
	}

}

//kotlin {
//	compilerOptions {
//		freeCompilerArgs.addAll("-Xjsr305=strict")
//	}
//}


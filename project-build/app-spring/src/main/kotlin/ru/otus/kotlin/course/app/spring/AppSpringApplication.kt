package ru.otus.kotlin.course.app.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// swagger URL: http://localhost:8080/swagger-ui.html
//              http://localhost:8080/swagger-ui/index.html

@SpringBootApplication
class AppSpringApplication

fun main(args: Array<String>) {
	runApplication<AppSpringApplication>(*args)
}



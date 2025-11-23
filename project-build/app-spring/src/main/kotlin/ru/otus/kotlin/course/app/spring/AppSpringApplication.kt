package ru.otus.kotlin.course.app.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// swagger URL: http://localhost:8080/swagger-ui.html
//              http://localhost:8080/swagger-ui/index.html

// actuator URL: http://localhost:8080/actuator
//				 http://localhost:8080/actuator/health
//				 http://localhost:8080/actuator/beans
//				 http://localhost:8080/actuator/threaddump

@SpringBootApplication
class AppSpringApplication

fun main(args: Array<String>) {
	runApplication<AppSpringApplication>(*args)
}



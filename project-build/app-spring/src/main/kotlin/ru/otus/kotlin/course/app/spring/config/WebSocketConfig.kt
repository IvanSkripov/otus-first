package ru.otus.kotlin.course.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import ru.otus.kotlin.course.app.spring.controllers.PsContollerWS

@Configuration
class WebSocketConfig (
    private val controller: PsContollerWS
) : WebFluxConfigurer {

    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/ws" to controller
        )

        return SimpleUrlHandlerMapping(handlerMap, -1)
    }

}
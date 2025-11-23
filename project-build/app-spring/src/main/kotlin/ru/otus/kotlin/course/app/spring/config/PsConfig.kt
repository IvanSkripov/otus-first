package ru.otus.kotlin.course.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.kotlin.course.app.spring.base.PsSettings
import ru.otus.kotlin.course.app.spring.base.PsWsRepo
import ru.otus.kotlin.course.app.spring.biz.PsStubProcessor
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.logger.PsLoggerProvider
import ru.otus.kotlin.course.common.worker.IPsProcessor

@Configuration
class PsConfig {

    @Bean
    fun processor(): IPsProcessor = PsStubProcessor()
    @Bean
    fun loggerProvider(): PsLoggerProvider = PsLoggerProvider()
    @Bean
    fun corSettings(): PsCoreSettings = PsCoreSettings(loggerProvider(), wsRepo())
    @Bean
    fun appSettings(corSettings: PsCoreSettings, processor: IPsProcessor) = PsSettings(corSettings, processor)
    @Bean
    fun wsRepo(): PsWsRepo = PsWsRepo()
    //@Bean
    //fun handlerAdapter() =  WebSocketHandlerAdapter()
}
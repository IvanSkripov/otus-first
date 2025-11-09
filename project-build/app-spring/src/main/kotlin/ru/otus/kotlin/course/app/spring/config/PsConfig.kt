package ru.otus.kotlin.course.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.kotlin.course.app.spring.base.PsSettings
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
    fun corSettings(): PsCoreSettings = PsCoreSettings(loggerProvider())
    @Bean
    fun appSettings(corSettings: PsCoreSettings, processor: IPsProcessor) = PsSettings(corSettings, processor)
}
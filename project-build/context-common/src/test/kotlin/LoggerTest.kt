package ru.otus.kotlin.course.common.test

import io.klogging.Level
import io.klogging.logger
import io.klogging.config.ANSI_CONSOLE
import io.klogging.config.loggingConfiguration
import io.klogging.noCoLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.kotlin.course.common.logger.PsLoggerProvider
import ru.otus.kotlin.course.common.logger.doWithLog

class LoggerTest {
    @Test
    fun checkLogger() {
        val logger = noCoLogger<LoggerTest>()
        logger.info("Test of KLogging")
        Thread.sleep(500)
    }

    @Test
    fun checkDoWithLog() {
        runBlocking {
            val fn: suspend () -> Int = { delay(1000); 1000 }
            val logger = PsLoggerProvider().logger("ru.otus.kotlin.checkDoWithLog")
            Thread.sleep(500)
            logger.doWithLog("fn", Level.INFO) {
                fn()
            }
        }
        Thread.sleep(500)
    }

    @Test
    fun checkDoWithError() {
        runBlocking {
            val logger = PsLoggerProvider().logger(LoggerTest::class)
            logger.error("Alles kaput")
        }
        Thread.sleep(500)
    }
}
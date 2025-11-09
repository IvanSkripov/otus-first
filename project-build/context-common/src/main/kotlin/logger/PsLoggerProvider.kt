package ru.otus.kotlin.course.common.logger

import io.klogging.Klogger
import kotlin.reflect.KClass

class PsLoggerProvider (
)  {
    fun logger(loggerId: String): Klogger = io.klogging.logger(loggerId)
    fun logger(clazz: KClass<*>): Klogger = io.klogging.logger(clazz.qualifiedName ?: clazz.simpleName ?: "unknown class")
}
//logProvider.logger(clazz)
// logger.info
//fun psLoggerLogback()
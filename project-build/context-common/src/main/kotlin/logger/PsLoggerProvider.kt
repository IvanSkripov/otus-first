package ru.otus.kotlin.course.common.logger

import kotlin.reflect.KClass

class PsLoggerProvider (
    private val provider: (String) -> IPsLogger  = { IPsLogger.DEFAULT }
) {
    fun logger(loggerId: String): IPsLogger = provider(loggerId)
    fun logger(clazz: KClass<*>): IPsLogger = provider(clazz.qualifiedName ?: clazz.simpleName ?: "unknown class")
}

//fun psLoggerLogback()
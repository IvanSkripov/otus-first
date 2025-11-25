package ru.otus.kotlin.course.common.logger

import io.klogging.Klogger
import io.klogging.Level
import kotlin.time.measureTimedValue

//class PsLogger (
//    logger:
//    loggerId: String
//) : IPsLogger{
//}

suspend fun <T> Klogger.doWithLog(
    event: String,
    lvl: Level,
    block: suspend () -> T
): T = try {
    log(lvl, "Start ${event}")
    val (res, diffTime) = measureTimedValue { block() }
    log(lvl, "Finished ${event}", mapOf("measureTimedValue" to diffTime.toIsoString()))
    res
} catch (e: Throwable) {
    log(Level.ERROR, "Failed ${event}",  e)
    throw e
}

suspend fun <T> Klogger.doWithError(
    event: String,
    throwRequired: Boolean = true,
    block: suspend () -> T
) = try {
    val result = block()
    result
} catch (e: Throwable) {
    log (
        Level.ERROR,
        "Failed ${event} Excepton ${e}",
        e
    )
    if (throwRequired) { throw e } else { null }
}
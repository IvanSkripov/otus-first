package ru.otus.kotlin.course.common.logger

import kotlin.time.Clock

interface IPsLogger: AutoCloseable {

    fun log (
        msg: String,
        lvl: LogLevel,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null
    )

    fun error(
        msg: String,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null
    ) = log(msg, LogLevel.ERROR, marker, e, data, objs)

    fun info(
        msg: String,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null
    ) = log(msg, LogLevel.INFO, marker, e, data, objs)

    fun debug(
        msg: String,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null
    ) = log(msg, LogLevel.DEBUG, marker, e, data, objs)

    override fun close() {}

    companion object {
        enum class LogLevel (
            private val levelInt: Int,
            private val levelStr: String,
        ) {
            ERROR(40, "ERROR"),
            WARN(30, "WARN"),
            INFO(20, "INFO"),
            DEBUG(10, "DEBUG"),
            TRACE(0, "TRACE");

            override fun toString(): String = levelStr
        }

        @OptIn(kotlin. time. ExperimentalTime::class)
        val DEFAULT = object: IPsLogger {
            override fun log(
                msg: String,
                lvl: LogLevel,
                marker: String,
                e: Throwable?,
                data: Any?,
                objs: Map<String, Any>?
            ) {
                val markerString = marker.takeIf { it.isNotBlank() }?.let{ " ($it)" }
                val args = listOfNotNull(
                    "${Clock.System.now()} [${lvl}]$markerString: $msg }",
                    e?.let { "${it.message ?: " Unknown reason"}: \n${it.stackTraceToString()}"},
                    data?.toString(),
                    objs?.toString()
                )

                println(args.joinToString("\n"))
            }

        }
    }
}
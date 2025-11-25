package ru.otus.kotlin.course.app.spring.base

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.mappers.asError
import kotlin.math.log
import kotlin.reflect.KClass
import kotlin.time.Clock

suspend inline fun PsSettings.controllerHelper(
    crossinline getRequest: suspend PsBeContext.() -> Unit,
    clazz: KClass<*>,
    logId: String,
): PsBeContext {
    val ctx = PsBeContext(
        // TODO: Добавить поле для установления времени
        // timeStart = Clock.System.now(),
    )
    val logger = corSettings.loggerProvider.logger(clazz)
    return try {
        ctx.getRequest()
        logger.info("Request $logId started", mapOf("BeContext" to ctx))
        processor.exec(ctx)
        logger.info("Request $logId finished", mapOf("BeContext" to ctx))
        ctx
    } catch (e: Throwable) {
        logger.error("Request $logId failed", e, mapOf("BeContext" to ctx))
        ctx.state = PsState.FAILING
        ctx.errors.add(e.asError("Processing failed"))
        ctx
    }
}
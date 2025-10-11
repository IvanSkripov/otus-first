package ru.otus.kotlin.course.app.spring.base

import ru.otus.kotlin.course.common.PsBeContext
import kotlin.reflect.KClass
import kotlin.time.Clock

//suspend inline fun <T> PsSettings.controllerHelper(
//    crossinline getRequest: suspend PsBeContext.() -> Unit,
//    crossinline toResponse: suspend PsBeContext.() -> T,
//    clazz: KClass<*>,
//    logId: String,
//): T {
//    val out = controllerHelper(getRequest, clazz, logId)
//    return out.toResponse()
//}

suspend inline fun PsSettings.controllerHelper(
    crossinline getRequest: suspend PsBeContext.() -> Unit,
    clazz: KClass<*>,
    logId: String,
): PsBeContext {
    val ctx = PsBeContext(
        // TODO: Добавить поле для установления времени
        // timeStart = Clock.System.now(),
    )
    return ctx
}
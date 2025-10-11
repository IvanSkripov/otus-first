package ru.otus.kotlin.course.app.spring.controllers

import org.springframework.web.bind.annotation.*
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.api.v1.models.IResponse
import ru.otus.kotlin.course.api.v1.models.ImageReadRequest
import ru.otus.kotlin.course.api.v1.models.ImageReadResponse
import ru.otus.kotlin.course.app.spring.base.PsSettings
import ru.otus.kotlin.course.app.spring.base.controllerHelper
import ru.otus.kotlin.course.common.stubs.stubReadFailedToTransport
import ru.otus.kotlin.course.mappers.fromTransport
import ru.otus.kotlin.course.mappers.toTransport
import kotlin.reflect.KClass

@RestController
@RequestMapping ("image")
class PsController (
    private val appSettings: PsSettings
) {
    @PostMapping("read")
    suspend fun read(@RequestBody request: ImageReadRequest): ImageReadResponse =
        process(appSettings, request, this::class, "read")

//    @GetMapping("read")
//    suspend fun read(): ImageReadResponse = stubReadFailedToTransport().second


    companion object {
        suspend inline fun <reified Q: IRequest, reified R: IResponse> process(
            appSettings: PsSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            { fromTransport(request) },
            clazz,
            logId
        ).toTransport() as R
    }
}


///image/create:
//post:
//--
//
//--
///image/link:
///image/download:
///images/update:
///image/delete:
///image/search:
//
///labels/list:
///tags/list:


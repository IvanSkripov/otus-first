package ru.otus.kotlin.course.app.spring.controllers

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.otus.kotlin.course.api.v1.models.*
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

    @PostMapping("create")
    suspend fun create(
        @RequestPart("meta") meta: ImageCreateRequest,
        @RequestPart("file") filePart: FilePart
    ): Mono<ResponseEntity<ImageCreateResponse>> {
         return DataBufferUtils.join(filePart.content())
            .map { dataBuffer ->
                val bytes = ByteArray(dataBuffer.readableByteCount())
                dataBuffer.read(bytes)
                DataBufferUtils.release(dataBuffer)
                bytes
            }.flatMap { bytes ->
                mono {
                    val r = appSettings.controllerHelper(
                        { fromTransport(meta, bytes)},
                        this::class,
                        "create"
                    ).toTransport()
                    ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(r as ImageCreateResponse)

                }
            }
    }

    @PostMapping("read")
    suspend fun read(@RequestBody request: ImageReadRequest): ImageReadResponse =
        process(appSettings, request, this::class, "read")

    @PostMapping("update")
    suspend fun update(@RequestBody request: ImageUpdateRequest): ImageUpdateResponse =
        process(appSettings, request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: ImageDeleteRequest): ImageDeleteResponse =
        process(appSettings, request, this::class, "delete")

    @PostMapping("link")
    suspend fun link(@RequestBody request: ImageLinkRequest): ImageLinkResponse =
        process(appSettings, request, this::class, "link")

    @PostMapping("download")
    suspend fun download(@RequestBody request: ImageDownloadRequest): Mono<ResponseEntity<ByteArray>> {
        val ctx = appSettings.controllerHelper(
            { fromTransport(request) },
            this::class,
      "download")
        return Mono.just(
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(ctx.response.file)
        )
    }

    @PostMapping("search")
    suspend fun search(@RequestBody request: ImageSearchRequest): ImageSearchResponse =
        process(appSettings, request, this::class, "search")


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

///image/download:
//
///labels/list:
///tags/list:


package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsWorkMode

// ==== From Transport STUB ===============

fun stubUpdateFromTransport(): Pair<ImageUpdateRequest, PsBeContext> {
    return Pair(
        ImageUpdateRequest(
            debug = PsImageStubsItems.DBG_WRONG,
            image = PsImageStubsItems.FULL_FROM_IMAGE
        ),

        PsBeContext(
            command = PsCommand.UPDATE,
            workMode = PsWorkMode.STUB,
            stubCase = PsStubs.WRONG_LINK,
            request = PsImageStubsItems.FULL_FROM_PSIMAGE
        )
    )
}

fun stubReadFromTransport(): Pair<ImageReadRequest, PsBeContext> {
    return Pair(
        ImageReadRequest(
            debug = PsImageStubsItems.DBG_WRONG,
            imageId = IMAGE_ID
        ),
        PsBeContext(
            command = PsCommand.READ,
            workMode = PsWorkMode.STUB,
            stubCase = PsStubs.WRONG_LINK,
            request = PsImageStubsItems.SIMPLE_REQUEST
        )
    )
}

fun stubDeleteFromTransport(): Pair<ImageDeleteRequest, PsBeContext> {
    return Pair(
        ImageDeleteRequest(
            debug = PsImageStubsItems.DBG_WRONG,
            imageId = IMAGE_ID
        ),
        PsBeContext(
            command = PsCommand.DELETE,
            workMode = PsWorkMode.STUB,
            stubCase = PsStubs.WRONG_LINK,
            request = PsImageStubsItems.SIMPLE_REQUEST
        )
    )
}

fun stubLinkFromTransport(): Pair<ImageLinkRequest, PsBeContext> {
    return Pair(
        ImageLinkRequest(
            debug = PsImageStubsItems.DBG_WRONG,
            imageId = IMAGE_ID
        ),
        PsBeContext(
            command = PsCommand.LINK,
            workMode = PsWorkMode.STUB,
            stubCase = PsStubs.WRONG_LINK,
            request = PsImageStubsItems.SIMPLE_REQUEST
        )
    )
}

fun stubDownloadFromTransport(): Pair<ImageDownloadRequest, PsBeContext> {
    return Pair(
        ImageDownloadRequest(
            debug = PsImageStubsItems.DBG_WRONG,
            imageId = IMAGE_ID
        ),
        PsBeContext(
            command = PsCommand.DOWNLOAD,
            workMode = PsWorkMode.STUB,
            stubCase = PsStubs.WRONG_LINK,
            request = PsImageStubsItems.SIMPLE_REQUEST
        )
    )
}

fun stubSearchFromTransport(): Pair<ImageSearchRequest, PsBeContext> {
    return Pair(
        ImageSearchRequest(
            debug = PsImageStubsItems.DBG_OK,
            search = ImageSearchObject(SEARCH_STRING)
        ),
        PsBeContext(
            command = PsCommand.SEARCH,
            workMode = PsWorkMode.TEST,
            stubCase = PsStubs.SUCCESS,
            filterString = SEARCH_STRING
        )
    )
}


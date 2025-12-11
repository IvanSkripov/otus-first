package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsState
import java.net.URI

// ==== To Transport STUB ===============
fun stubCreateToTransport(): Pair<PsBeContext, ImageCreateResponse> {
    return Pair (
        PsBeContext (
            command = PsCommand.CREATE,
            state = PsState.FINISHING,
            response = PsImage(
                id = PsImageId(IMAGE_ID)
            )
        ),
        ImageCreateResponse (
            result = ResponseResult.SUCCESS,
            imageId = IMAGE_ID
        )
    )
}

fun stubCreateFailedToTransport(): Pair<PsBeContext, ImageCreateResponse> {
    return Pair (
        PsBeContext (
            command = PsCommand.CREATE,
            state = PsState.FAILING,
            errors = ERORS_PS,
            response = PsImage(
                id = PsImageId(IMAGE_ID)
            )
        ),
        ImageCreateResponse (
            result = ResponseResult.ERROR,
            errors = ERORS,
            imageId = IMAGE_ID
        )
    )
}

fun stubReadToTransport(): Pair<PsBeContext, ImageReadResponse> {
    return Pair ( PsBeContext (
        command = PsCommand.READ,
        state = PsState.FINISHING,
        response = PsImageStubsItems.FULL_TO_PSIMAGE
    ),
        ImageReadResponse (
            result = ResponseResult.SUCCESS,
            image = PsImageStubsItems.FULL_TO_IMAGE
        )
    )
}

fun stubReadFailedToTransport(): Pair<PsBeContext, ImageReadResponse> {
    return Pair ( PsBeContext (
        command = PsCommand.READ,
        state = PsState.FAILING,
        errors = ERORS_PS,
    ),
        ImageReadResponse (
            result = ResponseResult.ERROR,
            errors = ERORS
        )
    )
}

fun stubLinkToTransport(): Pair<PsBeContext, ImageLinkResponse> {
    return Pair (
        PsBeContext (
            command = PsCommand.LINK,
            state = PsState.FINISHING,
            response = PsImage(
                id = PsImageId(IMAGE_ID),
                permanentLinkUrl = "www.google.com"
            )

        ),
        ImageLinkResponse (
            result = ResponseResult.SUCCESS,
            url = URI("www.google.com")
        )
    )
}

fun stubLinkFailedToTransport(): Pair<PsBeContext, ImageLinkResponse> {
    return Pair ( PsBeContext (
        command = PsCommand.LINK,
        state = PsState.FAILING,
        errors = ERORS_PS,
    ),
        ImageLinkResponse (
            result = ResponseResult.ERROR,
            errors = ERORS
        )
    )
}

fun stubUpdateToTransport(): Pair<PsBeContext, ImageUpdateResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.UPDATE,
            state = PsState.FINISHING,
            response = PsImage(id = PsImageId(IMAGE_ID))
        ),
        ImageUpdateResponse(
            result = ResponseResult.SUCCESS,
            imageId = IMAGE_ID
        )
    )
}

fun stubUpdateFailedToTransport(): Pair<PsBeContext, ImageUpdateResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.UPDATE,
            state = PsState.FAILING,
            errors = ERORS_PS

        ),
        ImageUpdateResponse(
            result = ResponseResult.ERROR,
            errors = ERORS
        )
    )
}

fun stubDeleteToTransport(): Pair<PsBeContext, ImageDeleteResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.DELETE,
            state = PsState.FINISHING,
            response = PsImage(id = PsImageId(IMAGE_ID))
        ),
        ImageDeleteResponse(
            result = ResponseResult.SUCCESS,
            imageId = IMAGE_ID
        )
    )
}

fun stubDeleteFailedToTransport(): Pair<PsBeContext, ImageDeleteResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.DELETE,
            state = PsState.FAILING,
            response = PsImage(id = PsImageId(IMAGE_ID)),
            errors = ERORS_PS
        ),
        ImageDeleteResponse(
            result = ResponseResult.ERROR,
            imageId = IMAGE_ID,
            errors = ERORS
        )
    )
}

fun stubSearchToTransport(): Pair<PsBeContext, ImageSearchResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.SEARCH,
            state = PsState.FINISHING,
            responseList = mutableListOf(PsImageStubsItems.FULL_TO_PSIMAGE, PsImageStubsItems.FULL_TO_PSIMAGE)
        ),
        ImageSearchResponse(
            result = ResponseResult.SUCCESS,
            list = listOf(PsImageStubsItems.FULL_TO_IMAGE, PsImageStubsItems.FULL_TO_IMAGE)
        )
    )
}

fun stubSearchFailedToTransport(): Pair<PsBeContext, ImageSearchResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.SEARCH,
            state = PsState.FAILING,
            errors = ERORS_PS,
            responseList = mutableListOf(PsImageStubsItems.FULL_TO_PSIMAGE, PsImageStubsItems.FULL_TO_PSIMAGE)
        ),
        ImageSearchResponse(
            result = ResponseResult.ERROR,
            list = listOf(PsImageStubsItems.FULL_TO_IMAGE, PsImageStubsItems.FULL_TO_IMAGE),
            errors = ERORS
        )
    )
}

fun stubTagsToTransport(): Pair<PsBeContext, TagsResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.TAGS,
            state = PsState.FINISHING,
            response = PsImage(
                id = PsImageId(IMAGE_ID),
                tags = TAGS
            )
        ),
        TagsResponse(
            result = ResponseResult.SUCCESS,
            tags = TAGS
        )
    )
}

fun stubTagsFailedToTransport(): Pair<PsBeContext, TagsResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.TAGS,
            state = PsState.FAILING,
            errors = ERORS_PS,
            response = PsImage(
                id = PsImageId(IMAGE_ID),
                tags = TAGS
            )
        ),
        TagsResponse(
            result = ResponseResult.ERROR,
            errors = ERORS,
            tags = TAGS
        )
    )
}

fun stubLabelsToTransport(): Pair<PsBeContext, LabelsResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.LABELS,
            state = PsState.FINISHING,
            response = PsImage(
                id = PsImageId(IMAGE_ID), // No need. Simple Test
                tags = TAGS,              // No need. Simple Test
                labels = LABELS_PS
            )
        ),
        LabelsResponse(
            result = ResponseResult.SUCCESS,
            labels = LABELS
        )
    )
}

fun stubLabelsFailedToTransport(): Pair<PsBeContext, LabelsResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.LABELS,
            state = PsState.FAILING,
            errors = ERORS_PS,
            response = PsImage(
                id = PsImageId(IMAGE_ID), // No need. Simple Test
                tags = TAGS,              // No need. Simple Test
                labels = LABELS_PS
            )
        ),
        LabelsResponse(
            result = ResponseResult.ERROR,
            errors = ERORS,
            labels = LABELS
        )
    )
}


fun stubDownloadFailedToTransport(): Pair<PsBeContext, ImageDownloadErrorResponse> {
    return Pair(
        PsBeContext(
            command = PsCommand.DOWNLOAD,
            state = PsState.FAILING,
            errors = ERORS_PS
        ),
        ImageDownloadErrorResponse(
            result = ResponseResult.ERROR,
            errors = ERORS
        )
    )
}

package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.*
import java.net.URI

private val IMAGE_ID = "123"
private val IMAGE_TITLE = "Update Title"
private val IMAGE_DESC = "Update Image Description"
private val SEARCH_STRING = "Search String is here"
private val TAGS = mutableListOf("good", "nice")
private val ERORS = listOf(ResponseErrorValue("1", "4", "2", "3"))
private val ERORS_PS = mutableListOf(PsError("1", "2", "3", "4"))
private val LABELS = listOf(Label("1", "2", "3"))
private val LABELS_PS = mutableListOf(PsLabel("1", "2", "3"))



fun <R: IRequest> prepareReq(req: R, block: R.() -> Unit): R {
    req.block()
    return req
}

fun <R: IResponse> prepareRsp(rsp: R, block: R.() -> Unit): R {
    rsp.block()
    return rsp
}

fun prepareCtx(ctx: PsBeContext, block: PsBeContext.() -> Unit): PsBeContext {
    ctx.block()
    return ctx
}

fun stubResponseError(stub: PsStubs ): PsBeContext {
    return prepareCtx(PsBeContext()) {
        state = PsState.FAILING
        errors.add(PsError(code = stub.toString(), message = "Message ${stub.toString()}"))
    }
}


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

// ==== STUB Helpers ===============

object PsImageStubsItems {
    val DBG_WRONG = DebugItem (
        mode = DebugItem.Mode.STUB,
        stub = DebugItem.Stub.WRONG_LINK)

    val DBG_OK = DebugItem (
        mode = DebugItem.Mode.TEST,
        stub = DebugItem.Stub.SUCCESS)

    val FULL_TO_PSIMAGE = PsImage(
        id = PsImageId(IMAGE_ID),
        title = IMAGE_TITLE,
        desc =  IMAGE_DESC,
        tags = TAGS,
        labels = LABELS_PS,
        imageUrl = "www.google.com",
        previewUrl = "www.yandex.ru"
    )

    val FULL_TO_IMAGE = Image (
        imageId = IMAGE_ID,
        preview = URI("www.yandex.ru"),
        image = URI("www.google.com"),
        title = IMAGE_TITLE,
        desc = IMAGE_DESC,
        tags = TAGS,
        labels = LABELS
    )

    val FULL_FROM_IMAGE = ImageItem(
        imageId = IMAGE_ID,
        title = IMAGE_TITLE,
        desc = IMAGE_DESC,
        tags = TAGS,
        labels = LABELS
    )

    val FULL_FROM_PSIMAGE = PsImage(
        id = PsImageId(IMAGE_ID),
        title = IMAGE_TITLE,
        desc = IMAGE_DESC,
        tags = TAGS,
        labels = LABELS_PS
    )

    val SIMPLE_REQUEST = PsImage(id = PsImageId(IMAGE_ID))

}
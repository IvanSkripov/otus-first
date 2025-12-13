package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.helpers.validationErr
import ru.otus.kotlin.course.common.models.*
import java.io.File
import java.net.URI
import java.net.URL

internal val IMAGE_ID = "123"
internal val IMAGE_TITLE = "Update Title"
internal val IMAGE_DESC = "Update Image Description"
internal val SEARCH_STRING = "Search String is here"
internal val PERM_LINK = "www.google.com"
internal val TAGS = mutableListOf("good", "nice")
internal val ERORS = listOf(ResponseErrorValue("1", "4", "2", "3"))
internal val ERORS_PS = mutableListOf(PsError("1", "2", "3", "4"))
internal val LABELS = listOf(Label("author", "Автор", "Александр Михайлович Родченко"))
internal val LABELS_PS = mutableListOf(PsLabel("author", "Автор", "Александр Михайлович Родченко"))
internal val BYTES = byteArrayOf(0x30, 0x31, 0x32)


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

fun getDefaultId() = IMAGE_ID

// ==== STUB Helpers ===============

object PsImageStubsItems {
    val DBG_WRONG = DebugItem (
        mode = DebugItem.Mode.STUB,
        stub = DebugItem.Stub.WRONG_LINK)

    val DBG_OK = DebugItem (
        mode = DebugItem.Mode.STUB,
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

    val DB_ERROR = PsError(
        code = "repo-internal-error",
        field = "*",
        group = "repo",
        message = "Internal DB error"
    )

    val WRONG_OWNER = PsError(
        code = "WrongOwner",
        group = "permissions",
        message = "Image doesn't belong to the owner (id.owner == visitor)"
    )

    val WRONG_LINK = PsError(
        code = "WrongLink",
        group = "uploading",
        message = "Wrong link for uploading image"
    )

    val WRONG_IMAGE_SIZE = PsError(
        code = "WrongImageSize",
        group = "uploading",
        message = "Size of the image is more than 100M"
    )

    val WRONG_IMAGE_FORMAT = PsError(
        code = "WrongImageFormat",
        group = "uploading",
        message = "Wrong format of the uploading image"
    )

    val SIMPLE_REQUEST = PsImage(id = PsImageId(IMAGE_ID))

}
package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.api.v1.models.*
import java.io.File
import java.net.URI

// ==== Full Cycle Stubs ===========

fun  stubRead(flag: Boolean = true) = Pair<ImageReadRequest, ImageReadResponse> (
    ImageReadRequest(
        requestType = "read",
        debug = PsImageStubsItems.DBG_OK,
        imageId = IMAGE_ID
    ), ImageReadResponse (
        responseType = if (flag) "read" else null,
        result = ResponseResult.SUCCESS,
        image = PsImageStubsItems.FULL_TO_IMAGE
    )
)

fun  stubUpdate(flag: Boolean = true) = Pair<ImageUpdateRequest, ImageUpdateResponse> (
    ImageUpdateRequest(
        debug = PsImageStubsItems.DBG_OK,
        image = PsImageStubsItems.FULL_FROM_IMAGE
    ), ImageUpdateResponse (
        responseType = if (flag) "update" else null,
        result = ResponseResult.SUCCESS,
        imageId = IMAGE_ID
    )
)

fun  stubDelete(flag: Boolean = true) = Pair<ImageDeleteRequest, ImageDeleteResponse> (
    ImageDeleteRequest(
        debug = PsImageStubsItems.DBG_OK,
        imageId = IMAGE_ID
    ), ImageDeleteResponse (
        responseType = if (flag) "delete" else null,
        result = ResponseResult.SUCCESS,
        imageId = IMAGE_ID
    )
)

fun  stubLink(flag: Boolean = true) = Pair<ImageLinkRequest, ImageLinkResponse> (
    ImageLinkRequest(
        debug = PsImageStubsItems.DBG_OK,
        imageId = IMAGE_ID
    ), ImageLinkResponse (
        responseType = if (flag) "link" else null,
        result = ResponseResult.SUCCESS,
        url = URI(PERM_LINK)
    )
)

fun  stubSearch(flag: Boolean = true) = Pair<ImageSearchRequest, ImageSearchResponse> (
    ImageSearchRequest(
        debug = PsImageStubsItems.DBG_OK,
        search = ImageSearchObject(SEARCH_STRING)
    ), ImageSearchResponse (
        responseType = if (flag) "search" else null,
        result = ResponseResult.SUCCESS,
        list = listOf(PsImageStubsItems.FULL_TO_IMAGE, PsImageStubsItems.FULL_TO_IMAGE)
    )
)

fun  stubDownload() = Pair<ImageDownloadRequest, ByteArray> (
    ImageDownloadRequest(
        debug = PsImageStubsItems.DBG_OK,
        imageId = IMAGE_ID
    ), BYTES
)

fun  stubCreate(flag: Boolean = true) = Pair<ImageCreateRequest, ImageCreateResponse> (
    ImageCreateRequest(
        debug = PsImageStubsItems.DBG_OK,
        image = ImageCreateObject(
            title = IMAGE_TITLE,
            source = ImageSourceObject( sourceValue = ImageSourceFile(
                sourceType = "file",
                file = File("file")
            )
            )
        )
    ),
    ImageCreateResponse(
        responseType = if (flag) "create"  else null,
        result = ResponseResult.SUCCESS,
        imageId = IMAGE_ID
    )
)

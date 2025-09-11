package ru.otus.kotlin.course.mappers


import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.*
import ru.otus.kotlin.course.common.stubs.PsStubs
import ru.otus.kotlin.course.mappers.exception.UnknownOperationException


fun PsBeContext.fromTransport(request: IRequest) = when (request) {
    is ImageReadRequest -> fromTransport(request)
    is ImageDeleteRequest -> fromTransport(request)
    is ImageDownloadRequest -> fromTransport(request)
    is ImageLinkRequest -> fromTransport(request)
    is ImageSearchRequest -> fromTransport(request)
    is ImageUpdateRequest -> fromTransport(request)
    else -> throw UnknownOperationException(request.requestType ?: "NULL")
}

private fun DebugItem.fromTransportToWorkMode(): PsWorkMode = when (mode) {
    DebugItem.Mode.PROD -> PsWorkMode.PROD
    DebugItem.Mode.TEST -> PsWorkMode.TEST
    DebugItem.Mode.STUB -> PsWorkMode.STUB
}

private fun DebugItem.fromTransportToStub(): PsStubs = when (stub) {
    DebugItem.Stub.SUCCESS -> PsStubs.SUCCESS
    DebugItem.Stub.WRONG_OWNER -> PsStubs.WRONG_OWNER
    DebugItem.Stub.WRONG_LINK -> PsStubs.WRONG_LINK
    DebugItem.Stub.WRONG_IMAGE_SIZE -> PsStubs.WRONG_IMAGE_SIZE
    DebugItem.Stub.WRONG_IMAGE_FORMAT -> PsStubs.WRONG_IMAGE_FORMAT
    null -> PsStubs.NONE
}

private fun ImageItem.toInternal(): PsImage {
    val image: PsImage = PsImage(
        id = PsImageId(imageId)
    )

    image.title = this.title ?: ""
    image.desc = this.desc ?: ""
    this.tags?.let {
        image.tags.addAll(it)
    }
    this.labels?.let {
        it.forEach { image.labels.add(PsLabel(it.key, it.desc ?: "", it.value ?: "")) }
    }
    return image;
}

private fun PsBeContext.fromTransport(req: ImageReadRequest) {
    command = PsCommand.READ
    request = PsImage(id = PsImageId(req.imageId))
    workMode = req.debug.fromTransportToWorkMode()
    stubCase = req.debug.fromTransportToStub()
}

private fun PsBeContext.fromTransport(req: ImageDeleteRequest) {
    command = PsCommand.DELETE
    request = PsImage(id = PsImageId(req.imageId))
    workMode = req.debug.fromTransportToWorkMode()
    stubCase = req.debug.fromTransportToStub()
}

private fun PsBeContext.fromTransport(req: ImageDownloadRequest) {
    command = PsCommand.DOWNLOAD
    request = PsImage(id = PsImageId(req.imageId))
    workMode = req.debug.fromTransportToWorkMode()
    stubCase = req.debug.fromTransportToStub()
}

private fun PsBeContext.fromTransport(req: ImageLinkRequest) {
    command = PsCommand.LINK
    request = PsImage(id = PsImageId(req.imageId))
    workMode = req.debug.fromTransportToWorkMode()
    stubCase = req.debug.fromTransportToStub()
}

private fun PsBeContext.fromTransport(req: ImageSearchRequest) {
    command = PsCommand.SEARCH
    filterString = req.search.searchCreateria
    workMode = req.debug.fromTransportToWorkMode()
    stubCase = req.debug.fromTransportToStub()
}

private fun PsBeContext.fromTransport(req: ImageUpdateRequest) {
    command = PsCommand.UPDATE
    request = req.image.toInternal()
    workMode = req.debug.fromTransportToWorkMode()
    stubCase = req.debug.fromTransportToStub()
}
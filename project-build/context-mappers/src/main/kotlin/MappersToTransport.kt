package ru.otus.kotlin.course.mappers

import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.*
import ru.otus.kotlin.course.mappers.exception.UnknownOperationException
import ru.otus.kotlin.course.mappers.exception.WrongStateException
import java.net.URI


fun PsBeContext.toTransport(): IResponse = when(command) {
    PsCommand.CREATE -> toTransportCreate()
    PsCommand.READ -> toTransportRead()
    PsCommand.UPDATE -> toTransportUpdate()
    PsCommand.DELETE -> toTransportDelete()
    PsCommand.LINK -> toTransportLink()
    PsCommand.DOWNLOAD -> toTransportDownloadFalse()
    PsCommand.SEARCH -> toTransportSearch()
    PsCommand.TAGS -> toTransportTags()
    PsCommand.LABELS -> toTransportLabels()
    PsCommand.NONE -> throw UnknownOperationException("${PsCommand.NONE}")
}

private fun PsBeContext.toTransportCreate(): ImageCreateResponse = ImageCreateResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    imageId = response.toTransportId()
)

private fun PsBeContext.toTransportRead(): ImageReadResponse = ImageReadResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    image = response.toTransportImageNullable()
)

private fun PsBeContext.toTransportUpdate(): ImageUpdateResponse = ImageUpdateResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    imageId = response.toTransportId()
)

private fun PsBeContext.toTransportDelete(): ImageDeleteResponse = ImageDeleteResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    imageId = response.toTransportId()
)

private fun PsBeContext.toTransportLink(): ImageLinkResponse = ImageLinkResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    url = response.toTransportPermLink()
)

private fun PsBeContext.toTransportTags(): TagsResponse = TagsResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    tags = response.tags.takeIf { it.isNotEmpty() }
)

private fun PsBeContext.toTransportLabels(): LabelsResponse = LabelsResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    labels = response.labels.takeIf { it.isNotEmpty() }?.map { it.toTransport()}?.toList()
)

private fun PsBeContext.toTransportSearch(): ImageSearchResponse = ImageSearchResponse (
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    list = responseList.map{ it.toTransportImage() }.toList().takeIf { it.isNotEmpty() }

)

private fun PsBeContext.toTransportDownloadFalse(): ImageDownloadErrorResponse {
    if (state.isPositive()) throw WrongStateException ("Command DOWNLOAD, but state is possiteve ")

    return ImageDownloadErrorResponse (
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        imageId = response.toTransportId()
    )
}

// ==========================================

private fun PsImage.toTransportImage(): Image = toTransportImageNullable()!!.let { it }

private fun PsImage.toTransportImageNullable(): Image? {
    if (id == PsImageId.NONE) return null

    return Image(
        imageId = id.asString(),
        image = URI(imageUrl),
        preview = URI(previewUrl),
        title = title,
        desc = desc,
        tags = tags.takeIf { tags.isNotEmpty() },
        labels = labels.map<PsLabel, Label>{
            Label(key = it.key, desc = it.desc, value = it.value)
        }.toList().takeIf { it.isNotEmpty() }
    )
}

private fun PsImage.toTransportPermLink(): URI? =
    this.permanentLinkUrl.takeIf { it.isNotEmpty() }?.let{ URI (it)}


private fun PsImage.toTransportId() : String? = this.id.takeIf { it != PsImageId.NONE  }?.asString()

private fun PsError.toTransport(): ResponseErrorValue = ResponseErrorValue (
    code = this.code,
    message = this.message,
    group = this.group,
    field = this.field
)

private fun List<PsError>.toTransportErrors(): List<ResponseErrorValue>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun PsState.toResult(): ResponseResult = when (this) {
    PsState.RUNNING -> ResponseResult.SUCCESS
    PsState.FAILING -> ResponseResult.ERROR
    PsState.FINISHING -> ResponseResult.SUCCESS
    PsState.NONE -> ResponseResult.ERROR
}

private fun PsLabel.toTransport(): Label = Label(
    key,
    desc,
    value
)

package ru.otus.kotlin.course.common.stubs.repo

import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel

data class ImageEntity (
    private val id: String,
    private val title: String = "",
    private val desc: String = "",
    private val tags: List<String> = listOf(),
    private val labels: List<PsLabel> = listOf(),

    private val uploadUrl: String = "",
    private val imageUrl: String = "",
    private val previewUrl: String = "",
    private val permanentLinkUrl: String = "",

    private val bytes: ByteArray = ByteArray(0)
) {

    constructor(image: PsImage): this (
        id = image.id.asString(),
        title = image.title,
        desc = image.desc,
        tags =  image.tags.toList(),
        labels = image.labels.toList(),
        uploadUrl = image.uploadUrl,
        imageUrl = image.imageUrl,
        previewUrl = image.previewUrl,
        permanentLinkUrl = image.permanentLinkUrl,
        bytes = image.file.copyOf()
    )

    fun toModel(): PsImage {
        val ret = PsImage(
            id = PsImageId(this.id) ,
            title = this.title,
            desc = this.desc,
            tags =  this.tags.toMutableList(),
            labels = this.labels.toMutableList(),
            uploadUrl = this.uploadUrl,
            imageUrl = this.imageUrl,
            previewUrl = this.previewUrl,
            permanentLinkUrl = this.permanentLinkUrl
        )
        ret.file = bytes.copyOf()
        return ret
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (desc != other.desc) return false
        if (tags != other.tags) return false
        if (labels != other.labels) return false
        if (uploadUrl != other.uploadUrl) return false
        if (imageUrl != other.imageUrl) return false
        if (previewUrl != other.previewUrl) return false
        if (permanentLinkUrl != other.permanentLinkUrl) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + labels.hashCode()
        result = 31 * result + uploadUrl.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + previewUrl.hashCode()
        result = 31 * result + permanentLinkUrl.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
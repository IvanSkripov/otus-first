import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel
import ru.otus.kotlin.course.repo.postgre.tables.pojos.Images

fun Images.toPsImage(tags: MutableList<String>, labels: MutableList<PsLabel>): PsImage {
    return PsImage(
        PsImageId(this.id),
        title = title ?: "",
        desc = description ?: "",
        uploadUrl = uploadurl ?: "",
        imageUrl = imageurl ?:  "",
        previewUrl = previewurl ?:  "",
        permanentLinkUrl = permanentlinkurl ?:  "",
        tags = tags.takeIf { it.size > 0 } ?: mutableListOf(),
        labels = labels.takeIf { it.size > 0 } ?: mutableListOf()
    )
}
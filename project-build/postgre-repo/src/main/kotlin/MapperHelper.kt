import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.repo.postgre.tables.pojos.Images

fun Images.toPsImage(): PsImage {
    return PsImage(
        PsImageId(this.id),
        title = title ?: "",
        desc = description ?: "",
        uploadUrl = uploadurl ?: "",
        imageUrl = imageurl ?:  "",
        previewUrl = previewurl ?:  "",
        permanentLinkUrl = permanentlinkurl ?:  "",
    )
}
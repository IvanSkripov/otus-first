package ru.otus.kotlin.course.common.repo

import ru.otus.kotlin.course.common.models.PsError
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId

const val ERROR_GROUP_REPO = "repo"

interface IDBResult

data class DBGetImage ( val image: PsImage): IDBResult {
}

data class DBGetImages ( val images: List<PsImage>): IDBResult {
}

data class DBError (
    val code: String = "Unknown",
    val msg: String = "Unknown",
    val field: String = "Unknown",
    val e: Throwable? = null
): IDBResult {
    fun asPsError() = PsError(code = code, group = ERROR_GROUP_REPO, field = field, message = msg, exception = e)
}

data class DBImageRequest (
    val image: PsImage
)

@JvmInline
value class DBImageId (
    private val imageId: String
) {
    fun isEmpty() = imageId == PsImageId.NONE.asString()
    fun asString() = imageId
    fun asPsImageId() = PsImageId(imageId)
}

fun PsImageId.toDB() = DBImageId(this.asString())

data class DBImageSearchFilter(
    private val criteria: String
) {
    fun asString() = criteria
}

val errorEmptyId = DBError(
    code = "$ERROR_GROUP_REPO-empty-id",
    field = "id",
    msg = "Id must not be null or blank"
)

fun errorNotFound (id: String)  = DBError(
    code = "$ERROR_GROUP_REPO-not-found",
    field = "id",
    msg = "Object with ID: ${id} is not Found",
)
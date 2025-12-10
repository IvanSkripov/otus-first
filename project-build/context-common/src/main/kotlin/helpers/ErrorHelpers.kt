package ru.otus.kotlin.course.common.helpers

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsError

val VALIDATION_CODE="validation"
val VALIDATION_GROUP="validation"

fun PsBeContext.fail (err: PsError) {
    errors.add(err)
}

fun validationErr(code: String, field: String = "", group: String = VALIDATION_GROUP, message: String = "" ) = PsError (
        code = "$VALIDATION_CODE-$code",
        field = field,
        group = group,
        message = message
)
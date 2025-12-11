package ru.otus.kotlin.course.common.worker

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings

interface IPsProcessor {
    suspend fun exec (ctx: PsBeContext)
}
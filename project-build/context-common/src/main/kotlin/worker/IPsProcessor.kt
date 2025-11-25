package ru.otus.kotlin.course.common.worker

import ru.otus.kotlin.course.common.PsBeContext

interface IPsProcessor {
    suspend fun exec (ctx: PsBeContext)
}
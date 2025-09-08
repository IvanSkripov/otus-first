package ru.otus.kotlin.course.common

import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsError
import ru.otus.kotlin.course.common.models.PsRequestId
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.stubs.PsStubs
import kotlin.time.Instant

data class PsBeContext (
    var command: PsCommand = PsCommand.NONE,
    var state: PsState = PsState.NONE,
    val errors: MutableList<PsError> = mutableListOf(),

    var workMode: PsWorkMode = PsWorkMode.PROD,
    var stubCase: PsStubs = PsStubs.NONE,

    var requestId: PsRequestId = PsRequestId.NONE,
    var timeStart: Instant = Instant.NONE

)
{ }
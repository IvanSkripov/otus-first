package ru.otus.kotlin.course.common

import ru.otus.kotlin.course.common.models.*
import ru.otus.kotlin.course.common.stubs.PsStubs


data class PsBeContext (
    var command: PsCommand = PsCommand.NONE,
    var state: PsState = PsState.NONE,
    val errors: MutableList<PsError> = mutableListOf(),

    var workMode: PsWorkMode = PsWorkMode.PROD,
    var stubCase: PsStubs = PsStubs.NONE,

    var requestId: PsRequestId = PsRequestId.NONE,
    //var timeStart: Instant =

    var request: PsImage = PsImage(),
    var filterString: String = "",

    var response: PsImage = PsImage(),
    var responseList : MutableList<PsImage> = mutableListOf()
)
{ }
package ru.otus.kotlin.course.common

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
@OptIn(ExperimentalTime::class)
val Instant.Companion.NONE
    get() = INSTANT_NONE
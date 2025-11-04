package ru.otus.kotlin.course.app.spring.base

import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.worker.IPsProcessor


data class PsSettings  (
    val corSettings: PsCoreSettings,
    val processor: IPsProcessor
)
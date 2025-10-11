package ru.otus.kotlin.course.common

import ru.otus.kotlin.course.common.logger.IPsLogger
import ru.otus.kotlin.course.common.ws.IWsSessionsRepo

data class PsCoreSettings (
    val loggerProvider: IPsLogger = IPsLogger(),
    val wsSession: IWsSessionsRepo = IWsSessionsRepo.NONE
) {
    companion object {
        val NONE = PsCoreSettings()
    }
}
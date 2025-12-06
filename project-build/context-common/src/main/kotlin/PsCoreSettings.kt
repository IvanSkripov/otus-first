package ru.otus.kotlin.course.common

import ru.otus.kotlin.course.common.logger.PsLoggerProvider
import ru.otus.kotlin.course.common.repo.IImageRepo
import ru.otus.kotlin.course.common.ws.IWsSessionsRepo

data class PsCoreSettings (
    val loggerProvider: PsLoggerProvider,
    val wsSessions: IWsSessionsRepo = IWsSessionsRepo.NONE,
    val repoTest: IImageRepo = IImageRepo.NONE,
    val repoProd: IImageRepo = IImageRepo.NONE,
) {
//    companion object {
//        val NONE = PsCoreSettings()
//    }
}
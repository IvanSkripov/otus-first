package ru.otus.kotlin.course.common.repo

abstract class ImageRepoBase: IImageRepo {
    suspend fun tryRun(block: suspend () -> IDBResult) = try {
            block()
        } catch (e: Throwable) {
            DBError(msg = "tryRun failed", e = e)
        }


}
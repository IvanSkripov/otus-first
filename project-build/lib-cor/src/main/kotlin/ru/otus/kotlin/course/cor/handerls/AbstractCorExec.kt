package ru.otus.kotlin.course.cor.handerls

import ru.otus.kotlin.course.cor.ICorExec

abstract class AbstractCorExec<T> (
    override val title: String,
    override val description: String,
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(e: Throwable) -> Unit = { e -> throw e }
) : ICorExec<T> {

    abstract suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (blockOn (context)) {
            try {
                handle (context)
            } catch (e: Throwable) {
                blockExcept(context, e)
            }
        }
    }
}

package ru.otus.kotlin.course.cor.handerls

import ru.otus.kotlin.course.cor.ICorExec

class CorChain<T> (
    override val title: String = "",
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(e: Throwable) -> Unit = { e -> throw e },
    private val workers: List<ICorExec<T>> = listOf()
) : AbstractCorExec<T> (title, description, blockOn, blockExcept)
{
    override suspend fun handle(context: T) {
       workers.forEach {
           it.exec(context)
       }
    }
}
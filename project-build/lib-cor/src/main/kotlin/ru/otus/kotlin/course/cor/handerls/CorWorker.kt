package ru.otus.kotlin.course.cor.handerls

class CorWorker<T> (
    override val title: String = "",
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {  },
    private val blockExcept: suspend T.(e: Throwable) -> Unit = { e -> throw e }
) : AbstractCorExec<T> (title, description, blockOn, blockExcept)
{
    override suspend fun handle(context: T) {
        blockHandle(context)
    }
}
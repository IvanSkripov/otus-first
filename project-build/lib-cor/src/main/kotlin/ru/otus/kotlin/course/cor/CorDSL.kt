package ru.otus.kotlin.course.cor

import ru.otus.kotlin.course.cor.handerls.CorChain
import ru.otus.kotlin.course.cor.handerls.CorWorker

@DslMarker
annotation class CorDSLMarker

@CorDSLMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String
    fun on(function: suspend T.() -> Boolean)
    fun except (function: suspend T.(e: Throwable) -> Unit)

    fun build() : ICorExec<T>
}

@CorDSLMarker
interface ICorWorkerDsl<T>: ICorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}
@CorDSLMarker
interface ICorChainDsl<T>: ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

@CorDSLMarker
abstract class CorExecDsl<T> : ICorExecDsl<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = {  e -> throw e }

    override var title: String = ""
    override var description: String = ""
    override fun on(function: suspend T.() -> Boolean)  {
        blockOn = function }
    override fun except (function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}

@CorDSLMarker
class CorWorkerDsl<T>: CorExecDsl<T> (), ICorWorkerDsl<T>  {

    var blockHandle: suspend T.() -> Unit = {  }
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker<T> (title, description, blockOn, blockHandle, blockExcept)

}

@CorDSLMarker
class CorChainDsl<T>: CorExecDsl<T> (), ICorChainDsl<T>  {

    protected var workers: MutableList<ICorExecDsl<T>> = mutableListOf()
    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> = CorChain<T> (title, description, blockOn, blockExcept, workers.map { it.build() })

}


fun <T> rootChain (function:  ICorChainDsl<T>.() -> Unit ): ICorChainDsl<T>  = CorChainDsl<T>().apply(function)


fun <T> ICorChainDsl<T>.worker (function: ICorWorkerDsl<T>.() -> Unit)  {
    add(CorWorkerDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.worker (title: String, description: String = "", function: T.() -> Unit)  {
    add(CorWorkerDsl<T>().also {
      it.title = title
      it.description = description
      it.handle (function)
    })
}

fun <T> ICorChainDsl<T>.chain (function: ICorChainDsl<T>.() -> Unit)  {
    add(CorChainDsl<T>().apply(function))
}
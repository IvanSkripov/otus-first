package ru.otus.kotlin.course.common.ws

interface IWsSessionsRepo  {
    fun add(session: IWsSession)
    fun clearAll()
    fun remove(session: IWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IWsSessionsRepo {
            override fun add(session: IWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}

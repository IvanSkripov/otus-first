package ru.otus.kotlin.course.app.spring.base

import ru.otus.kotlin.course.common.ws.IWsSession
import ru.otus.kotlin.course.common.ws.IWsSessionsRepo

class PsWsRepo() : IWsSessionsRepo {
    private val sessions: MutableSet<IWsSession> = mutableSetOf()
    override fun add(session: IWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IWsSession) {
        sessions.remove(session)
    }

    override suspend fun <K> sendAll(obj: K) {
        sessions.forEach() { it.send(obj)}
    }
}
package ru.otus.kotlin.course.app.spring.base

import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.kotlin.course.api.v1.apiResponseSerialize
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.api.v1.models.IResponse
import ru.otus.kotlin.course.common.stubs.stubResponseError
import ru.otus.kotlin.course.common.ws.IWsSession

class PsWsSession (
    private val session: WebSocketSession
) : IWsSession {
    override suspend fun <T> send(obj: T) {
        //TODO: отправка массива данных
        require(obj is IResponse)
        val message = apiResponseSerialize(obj)
        session.send((Mono.just(session.textMessage(message))))

    }
}
package ru.otus.kotlin.course.app.spring

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.core.publisher.Flux
import ru.otus.kotlin.course.api.v1.apiRequestSerialize
import ru.otus.kotlin.course.api.v1.apiResponseDeserialize
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.api.v1.models.IResponse
import java.net.URI
import java.nio.ByteBuffer
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference

abstract class AppWsBase() {

    abstract fun port(): Int

    protected fun <T, R : IResponse> sendAndReceive(
        data: T,
        block: (List<Any>) -> Unit
    ) = runBlocking {
        val client: WebSocketClient = ReactorNettyWebSocketClient()
        val uri = URI.create("ws://localhost:${port()}/ws")
        val actualRef = AtomicReference<List<Any>>()

        client.execute(uri) { webSocketSession: WebSocketSession ->
            webSocketSession
                .send(webSocketSession.produce(data))
                .thenMany(webSocketSession.receive().take(2).map { message ->
                    message.mapper()
                })
                .collectList()
                .doOnNext(actualRef::set)
                .then()
        }.block(Duration.ofSeconds(5))

        assertThat(actualRef.get()).isNotNull()
        val payload = actualRef.get().map { deserializeRs<R>(it) }
        assertThat(payload.size == 2)
        block(payload)
    }

    private fun <T> WebSocketSession.produce(param: T): Flux<WebSocketMessage> = when (param) {
        is ByteArray -> {
            listOf(param).asFlow().map {
                binaryMessage { factory ->
                    factory.wrap(ByteBuffer.wrap(it))
                }
            }.asFlux()
        }
        is IRequest -> {
            listOf(param).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }
        else -> throw IllegalArgumentException("Wrong type of message")
    }

    private fun WebSocketMessage.mapper(): Any {
        if (type == WebSocketMessage.Type.TEXT) {
            return getPayloadAsText(Charsets.UTF_8)
        }

        val buf = payload
        val bytes = ByteArray(buf.readableByteCount())
        buf.read(bytes)
        return bytes
    }

    protected fun <R: IRequest> serializeRq(r: R) = apiRequestSerialize(r)
    private fun <R: IResponse> deserializeRs(value: Any) = when(value) {
        is String -> apiResponseDeserialize<R>(value)
        else -> value
    }
}
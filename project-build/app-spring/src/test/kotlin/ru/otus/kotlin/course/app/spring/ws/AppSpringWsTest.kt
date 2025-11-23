package ru.otus.kotlin.course.app.spring.ws

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.kotlin.course.api.v1.*
import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.stubs.*
import java.net.URI
import java.nio.ByteBuffer
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertIs

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppSpringWsTest {

    @LocalServerPort
    var port: Int = 0

    @Test
    fun createImage() {
        val p = stubCreate(false)

        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {

            listOf(apiCreateRequestToBytes(CreateRequest(p.first,  byteArrayOf(0x31, 0x32, 0x33))))
                .asFlow()
                .map {
                    when(it) {
                        is ByteArray -> binaryMessage { factory ->
                            factory.wrap(ByteBuffer.wrap(it))
                        }
                        else -> throw IllegalArgumentException("Wrong type of message")
                    }
                }
                .asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertEquals(p.second, s)
        }
    }

    @Test
    fun readImage() {
        val p = stubRead(false)
        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {
            listOf(p.first).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertEquals(p.second, s)
        }
    }

    @Test
    fun updateImage() {
        val p = stubUpdate(false)
        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {
            listOf(p.first).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertEquals(p.second, s)
        }
    }

    @Test
    fun linkImage() {
        val p = stubLink(false)
        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {
            listOf(p.first).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertEquals(p.second, s)
        }
    }

    @Test
    fun deleteImage() {
        val p = stubDelete(false)
        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {
            listOf(p.first).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertEquals(p.second, s)
        }
    }

    @Test
    fun searchImage() {
        val p = stubSearch(false)
        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {
            listOf(p.first).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertThat(s).isEqualTo(p.second)
        }
    }

    @Test
    fun downloadImage() {
        val p = stubDownload()
        val f: WebSocketSession.() -> Flux<WebSocketMessage> = {
            listOf(p.first).asFlow().map { textMessage(serializeRq(it)) }.asFlux()
        }

        sendAndReceive <IResponse> (f) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertThat(s).isEqualTo(p.second)
        }
    }

    private fun <R: IResponse> sendAndReceive(produce: WebSocketSession.() -> Flux<WebSocketMessage>, block: (List<Any>) -> Unit) = runBlocking {
        val client: WebSocketClient = ReactorNettyWebSocketClient()
        val uri = URI.create("ws://localhost:$port/ws")
        val actualRef = AtomicReference<List<Any>>()

        client.execute(uri) { webSocketSession: WebSocketSession ->
            webSocketSession
                .send(webSocketSession.produce())
                .thenMany(webSocketSession.receive().take(2).map{message ->
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

    private fun WebSocketMessage.mapper(): Any {
        if (type == WebSocketMessage.Type.TEXT) {
            return getPayloadAsText(Charsets.UTF_8)
        }

        val buf = payload
        val bytes = ByteArray(buf.readableByteCount())
        buf.read(bytes)
        return bytes
    }
    private fun <R: IRequest> serializeRq(r: R) = apiRequestSerialize(r)
    private fun <R: IResponse> deserializeRs(value: Any) = when(value) {
        is String -> apiResponseDeserialize<R>(value)
        else -> value
    }
}
package ru.otus.kotlin.course.app.spring.ws

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.asFlux
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import ru.otus.kotlin.course.api.v1.*
import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.app.spring.AppWsBase
import ru.otus.kotlin.course.common.models.PsError
import ru.otus.kotlin.course.common.stubs.*
import ru.otus.kotlin.course.mappers.toTransport
import kotlin.test.assertEquals
import kotlin.test.assertIs

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppSpringWsTest : AppWsBase() {

    @LocalServerPort
    var port: Int = 0

    override fun port(): Int = port

    @Test
    fun createImage() {
        val p = stubCreate(false)
        val data = apiCreateRequestToBytes(CreateRequest(p.first,  byteArrayOf(0x31, 0x32, 0x33)))

        sendAndReceive <ByteArray, IResponse> (data) { pl ->
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
        sendAndReceive <ImageReadRequest, IResponse> (p.first) { pl ->
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

        sendAndReceive <ImageUpdateRequest, IResponse> (p.first) { pl ->
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

        sendAndReceive <ImageLinkRequest, IResponse> (p.first) { pl ->
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

        sendAndReceive <ImageDeleteRequest, IResponse> (p.first) { pl ->
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

        sendAndReceive <ImageSearchRequest, IResponse> (p.first) { pl ->
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

        sendAndReceive <ImageDownloadRequest, IResponse> (p.first) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertThat(s).isEqualTo(p.second)
        }
    }

    @Test
    fun errorsImage() {
        val items: List<Pair<DebugItem.Stub, PsError>> = listOf(
            Pair(
                DebugItem.Stub.WRONG_OWNER,
                PsImageStubsItems.WRONG_OWNER
            ),
            Pair(
                DebugItem.Stub.WRONG_LINK,
                PsImageStubsItems.WRONG_LINK
            ),
            Pair(
                DebugItem.Stub.WRONG_IMAGE_FORMAT,
                PsImageStubsItems.WRONG_IMAGE_FORMAT
            ),
            Pair(
                DebugItem.Stub.WRONG_IMAGE_SIZE,
                PsImageStubsItems.WRONG_IMAGE_SIZE
            )
        )

        items.forEach {
            val p = stubReadErrors(
                stubError = it.first,
                error = it.second.toTransport(),
                flag = false
            )

            sendAndReceive<ImageReadRequest, IResponse>(p.first) { pl ->
                val f = pl[0]
                val s = pl[1]
                println("---> ${f}")
                println("<--- ${s}")
                assertIs<WSInitResponse>(f)
                assertEquals(p.second, s)
            }
        }
    }
}
package ru.otus.kotlin.course.app.spring.repo

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.kotlin.course.api.v1.CreateRequest
import ru.otus.kotlin.course.api.v1.apiCreateRequestToBytes
import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.app.spring.AppWsBase
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertIs

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppSpringRepoTest: AppWsBase() {

    @LocalServerPort
    var port: Int = 0
    override fun port(): Int = port
    val UNKNOWN_ID = "XXXX"

    @Test
    fun imageProcessTest() {
        val cr = ImageCreateRequest (
            debug = DebugItem(
                mode = DebugItem.Mode.TEST,
            ),
            image = ImageCreateObject(
                title = "New Loaded File",
                source = ImageSourceObject( sourceValue = ImageSourceFile(
                    sourceType = "file",
                    file = File("file")
                ))
            )
        )
        val data = apiCreateRequestToBytes(CreateRequest(cr,  byteArrayOf(0x31, 0x32, 0x33)))
        var imageId = UNKNOWN_ID
        sendAndReceive <ByteArray, IResponse> (data) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            assertIs<ImageCreateResponse>(s)
            assertEquals(ResponseResult.SUCCESS, s.result)
            imageId = s.imageId ?: UNKNOWN_ID

        }

        assertTrue (imageId != UNKNOWN_ID)
        val rr = ImageReadRequest (
            debug = DebugItem(
                mode = DebugItem.Mode.TEST,
            ),
            imageId = imageId
        )

        sendAndReceive <ImageReadRequest, IResponse> (rr) { pl ->
            val f = pl[0]
            val s = pl[1]
            println("${f}")
            println("${s}")
            assertIs<WSInitResponse>(f)
            //assertEquals(cr.image.title, s)
        }
    }
}
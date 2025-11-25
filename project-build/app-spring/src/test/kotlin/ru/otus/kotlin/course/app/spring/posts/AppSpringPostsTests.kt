package ru.otus.kotlin.course.app.spring.posts

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.MultipartInserter
import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.app.spring.config.PsConfig
import ru.otus.kotlin.course.app.spring.controllers.PsContollerWS
import ru.otus.kotlin.course.app.spring.controllers.PsController
import ru.otus.kotlin.course.common.stubs.*



@WebFluxTest
@Import(PsController::class, PsConfig::class, PsContollerWS::class)
class AppSpringPostsTests (@Autowired private var webClient: WebTestClient) {

    @Test
    fun createImage () {

        val p = stubCreate()
        val body = LinkedMultiValueMap<String, Any>()
        body.add("meta", p.first)
        val content = byteArrayOf(0x31, 0x32, 0x33)
        val resource = ByteArrayResource(content)

        body.add("file",
                HttpEntity(
                    resource,
                    HttpHeaders().apply {
                        contentType = MediaType.APPLICATION_OCTET_STREAM
                        contentDisposition = ContentDisposition.formData()
                            .name("file")
                            .filename("test.txt")
                            .build()
                    }
                )
            )


        webClient
            .post()
            .uri("/image/create")
            .contentType(MediaType.MULTIPART_MIXED)
            .body(BodyInserters.fromMultipartData(body))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(ImageCreateResponse::class.java)
            .value {
                println("Response: $it")
                assertThat(it).isEqualTo(p.second)
            }
    }

    @Test
    fun readImage() {
        val p = stubRead()
        testStubHelper("/image/read", p.first, p.second)
    }

    @Test
    fun updateImage() {
        val p = stubUpdate()
        testStubHelper("/image/update", p.first, p.second)
    }

    @Test
    fun linkImage() {
        val p = stubLink()
        testStubHelper("/image/link", p.first, p.second)
    }

    @Test
    fun deleteImage() {
        val p = stubDelete()
        testStubHelper("/image/delete", p.first, p.second)
    }

    @Test
    fun searchImage() {
        val p = stubSearch()
        testStubHelper("/image/search", p.first, p.second)
    }

    @Test
    fun downloadImage() {
        val p = stubDownload()
        testStubHelper("/image/download", p.first, p.second)
    }

    private inline fun <reified Req: Any, reified Res: Any> testStubHelper(
        url: String, objRequest: Req, objResp: Res) {

        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(objRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("Response: $it")
                assertThat(it).isEqualTo(objResp)
            }
    }


}
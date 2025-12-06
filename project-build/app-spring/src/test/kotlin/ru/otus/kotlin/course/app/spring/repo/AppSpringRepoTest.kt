package ru.otus.kotlin.course.app.spring.repo

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import repo.SQLParams
import ru.otus.kotlin.course.api.v1.CreateRequest
import ru.otus.kotlin.course.api.v1.apiCreateRequestToBytes
import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.app.spring.AppWsBase
import ru.otus.kotlin.course.app.spring.config.PsConfig
import ru.otus.kotlin.course.app.spring.controllers.PsContollerWS
import ru.otus.kotlin.course.app.spring.controllers.PsController
import java.io.File
import java.sql.DriverManager
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

//@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PsController::class, PsConfig::class, PsContollerWS::class)
class AppSpringRepoTest: AppWsBase() {

    @LocalServerPort
    var port: Int = 0
    override fun port(): Int = port

    val UNKNOWN_ID = "XXXX"
    val CREATE_TITILE = "New Loaded File"
    val UPDATE_TITILE = "Updated Title"
    val DBG_TEST =  DebugItem(mode = DebugItem.Mode.TEST)
    val DBG_PROD =  DebugItem(mode = DebugItem.Mode.PROD)
    val BYTES = byteArrayOf(0x31, 0x32, 0x33)
    val TAGS = listOf("New", "Home")
    val LABELS = buildList<Label> {
        add(Label("author", "Автор", "Энни Лейбовиц"))
        add(Label("format", "Формат изображения", "PSD"))
    }
    var DEBUG_MODE = DBG_TEST

    @Test
    fun imageProcessTestMode() {

        DEBUG_MODE = DBG_TEST
        imageProcessTest()
    }

    @Test
    fun imageProcessProdMode() {
        DEBUG_MODE = DBG_PROD
        imageProcessTest()
    }

    fun imageProcessTest() {
        val cr = getCreateReq(CREATE_TITILE)
        val data = apiCreateRequestToBytes(CreateRequest(cr, BYTES))
        var imageId = UNKNOWN_ID
        sendAndReceive <ByteArray, IResponse> (data) { pl ->
            val res = checkResultResponse<ImageCreateResponse>(pl)
            imageId = res.imageId ?: UNKNOWN_ID
        }

        assertTrue (imageId != UNKNOWN_ID)
        val rr = getReadReq(imageId)

        sendAndReceive <ImageReadRequest, IResponse> (rr) { pl ->
            val res = checkResultResponse<ImageReadResponse>(pl)
            assertEquals(CREATE_TITILE, res.image?.title )
        }

        val ssOld = getSearchReq("Loaded")
        sendAndReceive <ImageSearchRequest, IResponse> (ssOld) { pl ->
            val res = checkResultResponse<ImageSearchResponse>(pl)
            assertEquals(1 , res.list?.size)
            assertEquals(CREATE_TITILE, res.list?.get(0)?.title )
        }

        val dr = getDownloadReq(imageId)
        sendAndReceive <ImageDownloadRequest, IResponse> (dr) { pl ->
            val res = checkResultBytes(pl)
            assertThat<ByteArray> (res).isEqualTo(BYTES)
        }

        val lr = getLinkReq(imageId)
        sendAndReceive <ImageLinkRequest, IResponse> (lr) { pl ->
            val res = checkResultResponse<ImageLinkResponse>(pl)
            assertTrue( res.url != null )
        }

        var result: Image? = null
        sendAndReceive <ImageReadRequest, IResponse> (rr) { pl ->
            val res = checkResultResponse<ImageReadResponse>(pl)
            result = res.image
        }

        assertNotNull(result)
        val ur = getUpdateReq(imageId, result as Image)
        sendAndReceive <ImageUpdateRequest, IResponse> (ur) { pl ->
            val res = checkResultResponse<ImageUpdateResponse>(pl)
        }

        sendAndReceive <ImageReadRequest, IResponse> (rr) { pl ->
            val res = checkResultResponse<ImageReadResponse>(pl)
            assertEquals(res.image?.title, UPDATE_TITILE)
            assertThat(res.image?.labels).isEqualTo(LABELS)
            assertThat(res.image?.tags).isEqualTo(TAGS)
        }

        val ssNew = getSearchReq("Updated")
        sendAndReceive <ImageSearchRequest, IResponse> (ssNew) { pl ->
            val res = checkResultResponse<ImageSearchResponse>(pl)
            assertEquals(res.list?.size , 1)
            assertEquals(UPDATE_TITILE, res.list?.get(0)?.title )
        }

        val del = getDeleteReq(imageId)
        sendAndReceive <ImageDeleteRequest, IResponse> (del) { pl ->
            val res = checkResultResponse<ImageDeleteResponse>(pl)
        }

        sendAndReceive <ImageReadRequest, IResponse> (rr) { pl ->
            val res = checkResultError<ImageReadResponse>(pl)
            println("[2] ---> ${res.errors?.get(0)}")
        }
    }

    private inline fun <reified T: IResponse> checkResultResponse(list: List<Any>): T {
        val f = list[0]
        val s = list[1]
        println("[0] ---> ${f}\n[1] ---> ${s}")
        assertIs<WSInitResponse>(f)
        assertIs<T> (s)
        assertEquals(ResponseResult.SUCCESS, s.result)
        assertTrue(s.errors?.isEmpty() ?: true)
        return s as T
    }

    private fun checkResultBytes(list: List<Any>): ByteArray {
        val f = list[0]
        val s = list[1]
        println("[0] ---> ${f}\n[1] ---> ${s}")
        assertIs<WSInitResponse>(f)
        assertIs<ByteArray> (s)
        return s as ByteArray
    }

    private inline fun <reified T: IResponse> checkResultError(list: List<Any>): T {
        val f = list[0]
        val s = list[1]
        println("[0] ---> ${f}\n[1] ---> ${s}")
        assertIs<WSInitResponse>(f)
        assertIs<T> (s)
        assertEquals(ResponseResult.ERROR, s.result)
        assertTrue(s.errors?.isNotEmpty()  ?: false)
        return s as T
    }

    private fun getCreateReq(title: String) = ImageCreateRequest (
        debug = DEBUG_MODE,
        image = ImageCreateObject(
            title = title,
            source = ImageSourceObject( sourceValue = ImageSourceFile(
                sourceType = "file",
                file = File("file")
            ))
        )
    )

    private fun getReadReq(imageId: String) = ImageReadRequest (
        debug = DEBUG_MODE,
        imageId = imageId
    )

    private fun getUpdateReq(imageId: String, image: Image) = ImageUpdateRequest (
        debug = DEBUG_MODE,
        image = ImageItem(
            imageId = image.imageId,
            title = UPDATE_TITILE,
            desc = image.desc,
            labels = LABELS,
            tags = TAGS
        )
    )

    private fun getDownloadReq(imageId: String) = ImageDownloadRequest (
        debug = DEBUG_MODE,
        imageId = imageId
    )

    private fun getLinkReq(imageId: String) = ImageLinkRequest (
        debug = DEBUG_MODE,
        imageId = imageId
    )

    private fun getDeleteReq(imageId: String) = ImageDeleteRequest (
        debug = DEBUG_MODE,
        imageId = imageId
    )

    private fun getSearchReq(search: String)  = ImageSearchRequest (
        debug = DEBUG_MODE,
        search = ImageSearchObject(searchCreateria = search)
    )


    companion object {
        //@JvmStatic
        val pg = PostgreSQLContainer(
            DockerImageName.parse("postgres:15.4")
                .asCompatibleSubstituteFor("postgres"))

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            pg.start()
            val changelog = "db/data-set-v0.yml"
            println("Migration starting. ChangeLog: [${changelog}] on DB url: ${pg.getJdbcUrl()}, user: ${pg.getUsername()}, password: ${pg.getPassword()}")
            val conn = DriverManager.getConnection(pg.getJdbcUrl(), pg.getUsername(), pg.getPassword())
            val database = liquibase.database.DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(JdbcConnection(conn))
            val resourceAccessor = ClassLoaderResourceAccessor(this::class.java.classLoader)
            val lb = Liquibase(changelog, resourceAccessor, database)
            lb.update("")

        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            pg.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        public fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("psql.url", pg::getJdbcUrl);
            registry.add("psql.user", pg::getUsername);
            registry.add("psql.password", pg::getPassword);
           }
        }



}
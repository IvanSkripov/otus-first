import org.junit.Test
import ru.otus.kotlin.course.api.v1.apiMapper
import ru.otus.kotlin.course.api.v1.apiRequestDeserialize
import ru.otus.kotlin.course.api.v1.apiRequestSerialize
import ru.otus.kotlin.course.api.v1.models.*
import java.net.URI
import kotlin.test.assertContains
import kotlin.test.assertEquals


class RequestSerializationTest {

    private val IMAGE_ID = "123"

    private val debug = DebugItem(
        mode = DebugItem.Mode.STUB,
        stub = DebugItem.Stub.WRONG_LINK
    )

//    private val requestCreate = ImageCreateRequest(
//        debug = this.debug,
//        image = ImageCreateObject(
//            title = "New Image",
//
//        )
//    )

    private val requestSearch = ImageSearchRequest(
        debug = this.debug,
        search = ImageSearchObject( searchCreateria =  "военнослужащий")
    )

    private val requestRead = ImageReadRequest(
        debug = this.debug,
        imageId = IMAGE_ID
    )

    private val requestUpdate = ImageUpdateRequest(
        debug = this.debug,
        image = ImageItem(
            title = "Update Title",
            desc = "Update Image Description",
            tags = listOf("good", "nice"),
            labels = listOf(Label("author", value = "WhoIsAuthor"))
        )
    )

    private val requestDelete = ImageDeleteRequest(
        debug = this.debug,
        imageId = IMAGE_ID
    )

    private val requestLink = ImageLinkRequest(
        debug = this.debug,
        imageId = "123"
    )

    private val requestDownload = ImageDownloadRequest(
        debug = this.debug,
        imageId = "123"
    )

    private fun <R: IRequest> deserialize(req: R) {
        val json = apiRequestSerialize(req)
        val obj = apiRequestDeserialize<R>( json )

        assertEquals(req, obj)
    }

    @Test
    fun  deserializeAll () {
        deserialize(requestSearch)
        deserialize(requestRead)
        deserialize(requestUpdate)
        deserialize(requestDelete)
        deserialize(requestLink)
        deserialize(requestDownload)
    }

    @Test
    fun serialize() {
        val json = apiRequestSerialize(requestUpdate)

        assertContains(json, Regex("\"title\":\\s*\"${requestUpdate.image?.title}\""))
        assertContains(json, Regex("\"desc\":\\s*\"${requestUpdate.image?.desc}\""))
        assertContains(json, Regex("\"stub\":\\s*\"wrongLink\""))
        assertContains(json, Regex("\"requestType\":\\s*\"update\""))


    }

    @Test
    fun serializeCreateObject() {
        val obj = ImageCreateObject(
            title = "Create Title",
            source = ImageSourceObject(
                sourceValue = ImageSourceLink(
                    sourceType = SOURCE_LINK,
                    link = URI("http://www.example.com/image.jpg")
                )
            )
        )

        val json = apiMapper.writeValueAsString(obj)
        assertContains(json, Regex("\"title\":\\s*\"${obj.title}\""))
        assertContains(json, Regex("\"sourceType\":\\s*\"${SOURCE_LINK}\""))
        assertContains(json, Regex("\"link\":\\s*\"http://www.example.com/image.jpg\""))

    }

}
import org.junit.Test
import ru.otus.kotlin.course.api.v1.apiMapper
import ru.otus.kotlin.course.api.v1.apiRequestDeserialize
import ru.otus.kotlin.course.api.v1.apiRequestSerialize
import ru.otus.kotlin.course.api.v1.models.DebugItem
import ru.otus.kotlin.course.api.v1.models.ImageCreateObject
import ru.otus.kotlin.course.api.v1.models.ImageCreateRequest
import java.net.URI
import kotlin.test.assertContains
import kotlin.test.assertEquals


class RequestSerializationTest {
    private val request = ImageCreateRequest(
        requestType = "mamba",
        debug = DebugItem(
            mode = DebugItem.Mode.STUB,
            stub = DebugItem.Stub.WRONG_LINK
        ),
        image = ImageCreateObject(
            title = "New Image",
            desc = "New Image Description",
            sourceType = "link",
            link = URI("http://www.example.com/image.jpg")
        )
    )

    @Test
    fun deserialize() {
        val json = apiRequestSerialize(request)
        val obj = apiRequestDeserialize<ImageCreateRequest>( json )

        assertEquals(request, obj)
    }

    @Test
    fun serialize() {
        val json = apiRequestSerialize(request)

        assertContains(json, Regex("\"sourceType\":\\s*\"link\""))
        assertContains(json, Regex("\"title\":\\s*\"${request.image?.title}\""))
        assertContains(json, Regex("\"desc\":\\s*\"${request.image?.desc}\""))
        assertContains(json, Regex("\"stub\":\\s*\"wrongLink\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

}
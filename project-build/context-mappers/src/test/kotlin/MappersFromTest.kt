import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.otus.kotlin.course.api.v1.models.DebugItem
import ru.otus.kotlin.course.api.v1.models.ImageItem
import ru.otus.kotlin.course.api.v1.models.ImageUpdateRequest
import ru.otus.kotlin.course.api.v1.models.Label
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.*
import ru.otus.kotlin.course.common.stubs.PsStubs
import ru.otus.kotlin.course.mappers.fromTransport
import kotlin.test.assertEquals


class MappersFromTest {

    private val IMAGE_ID = "123"
    private val IMAGE_TITLE = "Update Title"
    private val IMAGE_DESC = "Update Image Description"
    private val TAGS = mutableListOf("good", "nice")

    // TODO: Проверить все типы

    @Test
    fun fromTransportUpdate() {
        val requestUpdate = ImageUpdateRequest(
            debug = DebugItem(
                mode = DebugItem.Mode.STUB,
                stub = DebugItem.Stub.WRONG_LINK),
            image = ImageItem(
                imageId = IMAGE_ID,
                title = IMAGE_TITLE,
                desc = IMAGE_DESC,
                tags = TAGS,
                labels = listOf(Label("author", value = "WhoIsAuthor"))
            )
        )

        val expected = PsBeContext (
            command = PsCommand.UPDATE,
            workMode = PsWorkMode.STUB,
            stubCase = PsStubs.WRONG_LINK,
            request = PsImage(
                id = PsImageId(IMAGE_ID),
                title = IMAGE_TITLE,
                desc = IMAGE_DESC,
                tags = TAGS,
                labels = mutableListOf(PsLabel("author", value = "WhoIsAuthor"))
            )
        )

        val obj = PsBeContext()
        obj.fromTransport(requestUpdate)

        assertEquals(obj, expected)
    }


}
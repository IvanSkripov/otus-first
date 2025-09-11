import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.otus.kotlin.course.api.v1.models.*
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.*
import ru.otus.kotlin.course.common.stubs.*
import ru.otus.kotlin.course.mappers.exception.WrongStateException
import ru.otus.kotlin.course.mappers.fromTransport
import ru.otus.kotlin.course.mappers.toTransport
import java.net.URI
import kotlin.test.assertEquals


class MappersToTest {

    private val items: List<Pair<PsBeContext, IResponse>> = listOf(
        stubCreateToTransport(),
        stubCreateFailedToTransport(),
        stubReadToTransport(),
        stubReadFailedToTransport(),
        stubLinkToTransport(),
        stubLinkFailedToTransport(),
        stubUpdateToTransport(),
        stubUpdateFailedToTransport(),
        stubDeleteToTransport(),
        stubDeleteFailedToTransport(),
        stubSearchToTransport(),
        stubSearchFailedToTransport(),
        stubTagsToTransport(),
        stubTagsFailedToTransport(),
        stubLabelsToTransport(),
        stubLabelsFailedToTransport(),
        // No positive scenario
        stubDownloadFailedToTransport(),

    )


    @Test
    fun toTransport() {
        items.forEach {
            println("${it.first}\n\t\t ${it.second}")
            assertEquals(it.first.toTransport(), it.second)
        }
    }

    @Test
    fun toTransportDownload() {
        val ctx = prepareCtx(PsBeContext()) {
            command = PsCommand.DOWNLOAD
            state = PsState.FINISHING
        }
        assertThrows<WrongStateException> () {
            ctx.toTransport()
        }
    }
}
import org.junit.jupiter.api.Test
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.stubs.*
import ru.otus.kotlin.course.mappers.fromTransport
import kotlin.test.assertEquals


class MappersFromTest {

    private val items: List<Pair<IRequest, PsBeContext>> = listOf(
        stubUpdateFromTransport(),
        stubReadFromTransport(),
        stubDeleteFromTransport(),
        stubLinkFromTransport(),
        stubDownloadFromTransport(),
        stubSearchFromTransport(),
    )


    @Test
    fun fromTransport() {
        items.forEach {
            println("${it.first}\n\t\t ${it.second}")
            val obj = PsBeContext()
            obj.fromTransport(it.first)
            assertEquals(obj, it.second)

        }
    }
}
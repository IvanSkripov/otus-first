import org.junit.Test
import ru.otus.kotlin.course.api.v1.*
import ru.otus.kotlin.course.api.v1.models.ImageCreateResponse
import ru.otus.kotlin.course.api.v1.models.ResponseErrorValue
import ru.otus.kotlin.course.api.v1.models.ResponseResult
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {

    // TODO дописать сериализацию для всех видов ответов

    private val responseError = ImageCreateResponse(
        result = ResponseResult.ERROR,
        errors = listOf( ResponseErrorValue("101", "Wrong request"))
    )

    private val responseOk = ImageCreateResponse(
        result = ResponseResult.SUCCESS,
        imageId = "123"
    )

    @Test
    fun deserialize() {
        val jsonOk = apiResponseSerialize(responseOk)
        val objOk = apiResponseDeserialize<ImageCreateResponse>(jsonOk)

        assertEquals(responseOk, objOk)

        val jsonErr = apiResponseSerialize(responseError)
        val objErr = apiResponseDeserialize<ImageCreateResponse>(jsonErr)

        assertEquals(responseError, objErr)
    }

    @Test
    fun serialize() {
        val jsonOk = apiResponseSerialize(responseOk)
        assertContains(jsonOk, Regex("\"result\":\\s*\"success\""))
        assertContains(jsonOk, Regex("\"imageId\":\\s*\"${responseOk.imageId}\""))

        val jsonErr = apiResponseSerialize(responseError)
        assertContains(jsonErr, Regex("\"result\":\\s*\"error\""))
        assertContains(jsonErr, Regex("\"message\":\\s*\"${responseError.errors?.get(0)?.message}\""))
    }

}
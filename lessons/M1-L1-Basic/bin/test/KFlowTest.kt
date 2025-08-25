import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class KFlowTest
{

    val x = 1
    val DO_THIS: String = "Do this"
    val DONT_THIS: String = "Do NOT do this"

    @Test
    fun whenBasic() {
        val text = when (x) {
            1 -> DO_THIS
            else -> DONT_THIS
        }
        assertIs<String> (text)
        assertEquals(text, DO_THIS)

        when(x) {
            1 -> println(DO_THIS)
            else -> println(DONT_THIS)
        }
    }

    @Test
    fun ternaryOperator() {
        val a = if (x == 1) { 2 } else { 3}
        assertEquals(a, 2)
        println("a = ${a}")
    }
}
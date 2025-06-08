import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import kotlin.test.assertFails

class KExpTest {

    @Test
    fun simpleExp () {
        assertFails { throw Exception("my excepton") }

        assertThrows<Exception> {
            throw Exception("my excepton")
        }
    }

    @Test
    fun catchExp() {
        try {
            throw Exception("my exception")
        } catch ( ex: RuntimeException) {
            println ("ex (RuntimeException) = ${ex.message}")
        } catch (e: Throwable) {
            println ("ex (Throwable) = ${e.message}")
        } finally {
            println("finaly")
        }
    }

    @Test
    fun expressionExp() {
        val a = try {
            throw Exception("my exception")
        } catch ( ex: RuntimeException) {
            println ("ex (RuntimeException) = ${ex.message}")
        } catch (e: Throwable) {
            1
        }
        println("expressionExp = ${a}")
    }
}
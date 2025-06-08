import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

class KTypesTest {
    @Test
    fun declareVars() {
        val b : Byte = 1
        assertIs<Byte>(b)
        assertIs<Int>(1)
        assertIs<Long>(b.toLong())
        assertIs<UInt>(b.toUInt())

        val arePeopleImmortal = false
        assertIs<Boolean>(arePeopleImmortal)

        val c = 'a'
        assertIs<Char>(c)
        assertIs<Char>('b')
    }

    @Test
    fun playWithArray() {
        val ar = arrayOf(1, 2, 3)
        assertIs<Array<Int>>(ar)

        for (i in ar) {
            println("Item: ${i}")
        }

        val ra = arrayOf(3, 2, 1)
        for ((idx, value) in ra.withIndex()) {
            println("Item ${idx} = ${value}")
        }

        println(ar.contentToString())
    }

    @Test
    fun playWithCast() {
        val s = "hello"
        assertIs<String>(s)
        println("String = ${s is String} ")

        var a: Any = 3.14
        println("Fload = ${a is Float} but Double = ${a is Double}")

        var b = a as? Float
        when (b) {
            null -> println("true")
            else -> println("funny")
        }
    }

    @Test
    fun kotlinTest1() {
        val whatTypeIAm = 3
        assertIs<Int>(whatTypeIAm)

        val corrrectType: Double = 3.14
        val anotherType: Float = 3.14f

        val whatTheResult: Long = 3L

        val str: String = "someString"
        val ch: Char = str[2]
        assertEquals(ch, 'm')

        val someAny: Any = "hello"
        assertFalse { someAny is Double }
    }
}
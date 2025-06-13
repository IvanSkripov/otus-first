import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KFuncTest {

    @Test
    /*
        1. значение по умолчанию
        2. именнованные аргументы
        3. vararg
        4. Unit
        5. Nothing
    */
    fun basicTest() {

        println(arg(a = 2, b = 3))
        println(arg(a = 1, 1))
        println(arg(1, 1, c=3))

        assertEquals(varargs_(1), 1)
        assertEquals(varargs_(1, 1), 2)
        val arr = intArrayOf(1, 2, 3, 4)
        assertEquals(varargs_(*arr), 10)
        assertEquals(varargs_(), 0)

        assertEquals(args(1), "Ein")
        assertEquals(args(3), "Polizei")

        val x = 2
        val y = when(x) {
            2 -> 2.0
            3 -> raiseAlways()
            else -> deduce()
        }
        assertEquals(y , 2.0)
        println("y typeOf = ${y::class.qualifiedName} ")

        assertThrows<java.lang.RuntimeException>() { raiseAlways()}
    }

    private fun arg(a: Int, b: Int, c: Int=2): Int = a+b+c

    private fun varargs_(vararg args: Int): Int {
        return if (args.isEmpty()) { 0 } else { args.reduce() { acc, value -> acc + value }}
    }

    private fun args(a: Int) = when(a) {
        1 -> "Ein"
        2 -> "Zwei"
        else -> "Polizei"
    }

    private fun deduce() { }

    private fun raiseAlways(): Nothing = throw RuntimeException("Raise it now")

    @Test
    fun genericsTest() {
        assertEquals(listOfTwo(5, 10).get(0), 5)
        assertEquals(listOfTwo(5, 10).get(1), 10)
        assertNull(listOfTwo(null, 10).get(0) )
        assertEquals(myMax(2, 3),3)
        assertEquals(myMax(3, 2),3)

    }

    private fun <T> listOfTwo(a: T, b: T) = listOf(a , b)
    private fun <T: Comparable<T> > myMax(x: T, y: T) = if (x > y) { x } else { y }

    @Test
    fun reifiedTest() {
        typedGeneric(1)
        typedGeneric(2.0)
        typedGeneric("String")
        typedGeneric(listOf("A"))
    }

    inline fun <reified T:Any> typedGeneric(arg: T) {
        println("type: ${arg::class::qualifiedName} of ${T::class}")
    }

    @Test
    fun lambdaTest() {
        val pow = { a : Int -> a * a }
        assertEquals(pow(2), 4)
        assertEquals(pow(3), 9)

        val mul : (Int, Int) -> String = { a: Int, b: Int -> (a * b).toString() }
        assertEquals(mul(2, 2), "4")
        assertEquals(mul(1, 2), "2")

        sayThis("Angelika") { "Hello, ${it}" }
        val byeIt = {value : String -> "Goodbye, ${value}" }
        sayThis("Ivan", byeIt)
        sayThis("John", ::talk)
    }

    private fun talk(a: String): String = "Say - ${a}"

    private fun sayThis (name: String, talk: (String) -> String) {
        println(talk(name))
    }

    @Test
    fun extensionTest() {
        val a = 1
        println("convertIt = ${a.convertIt()}")

        val ups:  String.() -> String = { "${this}-${this}"}
        assertEquals("doom".ups(), "doom-doom")

        val b = buildIntArray {
            add(1)
            add(2)
            removeFirst()
            add(3)
        }

        println("b typeOf ${b::class.qualifiedName} is ${b.toHumanString()}")

        val c: IntArray = intArrayOf(1, 2, 3, 4)
        println("c typeOf ${c::class.qualifiedName} is ${c.toHumanString()}")

    }

    private fun Int.convertIt():String = "${this}"

    private fun buildIntArray (builder: MutableList<Int>.() -> Unit) : IntArray {
        var a = mutableListOf<Int>()
        a.builder()
        return a.toIntArray()
    }

    private fun IntArray.toHumanString(): String {
        if (this.isEmpty()) { return "[ ]"}
        var s: String = "[" + this.get(0)

        this.forEachIndexed { index, i -> if (index != 0) { s = s + ", ${i}"}  }

        return s + "]"
    }
}
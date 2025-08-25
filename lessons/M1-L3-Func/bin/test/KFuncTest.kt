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

        val wow: String.(String) -> Int = { s: String ->
            val a = this.toInt()
            val b = s.toInt()
            a+b
        }

        assertEquals("1".wow("2"), 3)
        assertThrows<NumberFormatException>() { "a".wow("2") }
        assertThrows<NumberFormatException>() { "1".wow("a") }

        val greet: String.(String) -> String = { "Hello, ${this} ${it}" }
        assertEquals("Ivan".greet("Skripov"), "Hello, Ivan Skripov")

        val str = "Ivan".greetWithSN("Fedorovich", "Skripov") { a: String, b: String -> "${a} ${b}" }
        println(str)
        assertEquals(str, "Hello, Ivan Fedorovich Skripov")

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

    private fun String.greetWithSN (second: String, family: String, getter: String.(String, String) -> String): String {
        return "Hello, ${this} ${getter(second, family)}"
    }

    @Test
    /*
        1. infix
        2. повторение мать учения
     */
    fun infixTest() {
        val a = "Karl" doom 5
        println (a)

        val x = 2 power 3
        val y = 2.0 power 3
        println ("x = ${x}, y = ${y}")
        assertEquals(x, 8)
        assertEquals(y, 8.0)

        assertEquals(10 power 0, 1)
        assertEquals(2 power 1, 2)
        assertEquals(2 power 10, 1024)

        val z = func("string") { "prefix: ${it}" }
        println(z)
        assertEquals(z, "lambda (prefix: string)")
    }

    infix fun String.doom(to: Int): String = "${this}, dooms day after ${to} days"

    infix fun Int.power(n: Int): Int {
        return internalPower<Int>( this, n, { 1 }, { acc , value  -> acc * value})
    }

    infix fun Double.power(n: Int): Double {
        return internalPower<Double>(this, n, { 1.0 },  { acc , value  -> acc * value}
        )
    }

    private fun <T: Number> internalPower(initial: T, n: Int, init: () -> T, mul: (acc: T, value: T) -> T ): T {
        var res: T = init();
        var times = n
        while (times-- > 0) {
            res = mul(res, initial)
        }
        return res
    }

    private fun func(arg: String, block: (String) -> String) = "lambda (${block(arg)})"


}

fun seq() {
    val s = sequence {
        yield(1)
    }
}
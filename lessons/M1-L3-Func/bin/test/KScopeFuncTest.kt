import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/*
    Написать свои варианты scope functions
 */
class KScopeFuncTest {

    @Test
    fun letTest () {
        assertEquals("4456", "445".let_ { it + "6" })

        val numbers = mutableListOf("one", "two", "three", "four", "five")
        numbers.map { it.length }.filter { it > 3 }.let_ {
            println(it)
        }
    }

    //  The context object is available as an argument (it).
    //  The return value is the lambda result.
    private fun<T, R> T.let_(block: (T) -> R): R {
        return block(this)
    }

    /* ------------------------------------------------------- */
    @Test
    fun withTest() {
        assertEquals("4456", with_("445") { this + "6" })

        val numbers = mutableListOf("one", "two", "three")
        with_(numbers) {
            println("'with' is called with argument $this")
            println("It contains $size elements")
        }
    }

    // The context object is available as a receiver (this).
    // The return value is the lambda result.
    private fun<R,RR> with_(receiver: R, block: R.() -> RR): RR {
        return receiver.block()
    }

    /* ------------------------------------------------------- */
    @Test
    fun applyTest() {
        assertEquals("445", "445".apply_ { this + "6" })

        val adam = Person("Adam").apply_ {
            age = 32
            location = "London"
        }
        println(adam)

        val ivan = Person("Ivan").apply_ {
            age = 49
            location = "Москва"
        }
        println(ivan)
    }

    // The context object is available as a receiver (this).
    // The return value is the object itself.
    private fun<T> T.apply_(block: T.() -> Unit): T {
        this.block()
        return this
    }

    /* ------------------------------------------------------- */
    @Test
    fun runTest() {
        assertEquals("4456", "445".run_ { this + "6" })

        val str = "Hello"
        str.run_ {
            println("The string's length: $length")
            println("The substring from 2: ${this.substring(2)}") // does the same
        }
    }

    // The context object is available as a receiver (this).
    // The return value is the lambda result.
    private fun<T, R> T.run_ (block: T.() -> R): R {
        return this.block()
    }

    /* ------------------------------------------------------- */
    @Test
    fun alsoTest() {
        assertEquals("445", "445".also_ { it + "6" })

        val numbers = mutableListOf("one", "two", "three")
        numbers
            .also_ { println("The list elements before adding new one: $it") }
            .add("four")

        numbers.also_ { println("The list elements after adding new one: $it") }

    }

    // The context object is available as an argument (it).
    // The return value is the object itself.
    private fun<T> T.also_(block: (T) -> Unit) : T {
        block(this)
        return this
    }


}
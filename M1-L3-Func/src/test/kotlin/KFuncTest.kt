import org.junit.jupiter.api.Test

class KFuncTest {

    @Test
    fun basicTest() {
        println(arg(a = 2, b = 3))
        println(arg(a = 1, 1))
        println(arg(1, 1, c=3))


    }

    private fun arg(a: Int, b: Int, c: Int=2): Int = a+b+c

}
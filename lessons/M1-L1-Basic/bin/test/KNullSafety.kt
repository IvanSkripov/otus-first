import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KNullSafety {

    @Test
    fun nullSafety() {
        // Безопасные вызовы ?.
        var canBeNull: Int? = 13
        val a = canBeNull?.toLong();
        assertEquals(a, 13L)

        // Элвис оператор ?:
        canBeNull = null
        val b: Long = canBeNull?.toLong() ?: 0L
        assertEquals(b, 0L)

        // Оператор !! - Антипаттерн!
        val ex = assertThrows<NullPointerException> {
            val c = canBeNull!!.toLong() // throws NPE
        }
        println("ex = [${ex.stackTraceToString()} ]")

        var nullVar: Nothing? = null
        var whatTypeAmI = nullVar?.toDouble()
        assertNull(whatTypeAmI)
        assertIs<Double?>(whatTypeAmI)

        val quessType = whatTypeAmI ?: 4.5
        assertNotNull(quessType)
        assertIs<Double> (quessType)

        val nullVar2 = null
        val quessType2 = nullVar2?.toLong() ?: 5
        assertEquals(quessType2, 5)
        assertIs<Long>(quessType2)

        val quessType3 = quessType?.toInt() ?: 2
        assertIs<Int> (quessType3)
        assertEquals(quessType3, 4)


    }

    @Test
    fun elvisProblem () {
        var str: String? = null
        print("Random = ")
        for (i in 0..10) {
            val r = Random.nextInt(0, 2)
            print(r)
            when (r) {
                1 -> str = "String type"
                0 -> str = null
            }
        }
        print("\n")


        val quessType = str ?: 4L
        assertIs<Any>(quessType)
        println ("quessType = ${quessType::class::qualifiedName}")
    }

    @Test
    fun asProblem() {
        var anyType: Any = 12
        var castFromAny = anyType as? String
        assertIs<Int?> (castFromAny)
        assertIs<String?> (castFromAny)

        when (castFromAny) {
            is Int? -> println("Type = Int?")
            is String? -> println("Type = String?")
        }

        // castFromAny = 4.2
    }

    @Test
    fun smartCast() {
        val a: Long? = 13L

        if (a != null) {
            var b = a + 180L
        }
    }
}
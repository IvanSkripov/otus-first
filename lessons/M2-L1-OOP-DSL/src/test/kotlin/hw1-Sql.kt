@file:Suppress("unused")

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class Hw1Sql {

    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)

    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where { "col_a" eq "id" }
            //where ( "col_a" eq "id" )
        }

        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where {
                "col_a" nonEq 0
            }
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null)"

        val real = query {
            from("table")
            where {
                or (
                    "col_a" eq 4,
                    "col_b" nonEq null
                )
            }
        }

        println(real.build())
        checkSQL(expected, real)
    }
}

class SqlSelectBuilder {

    fun build(): String {
        require(fromClause != null) { "fromClause shouldn't be null" }
        val s = if (selectClause.isEmpty()) "*" else selectClause.joinToString()
        val where = whereClause?.let { " where ${it.build()}"} ?:  ""
        return "select ${s} from ${fromClause}${where}"
    }

    private var fromClause: String? = null
    fun from (fromClause: String) {
        this.fromClause = fromClause
    }

    private var selectClause = mutableListOf<String>()
    fun select (vararg selectClause: String) {
        this.selectClause.addAll(selectClause.asList())
    }

    private var whereClause: SqlExpression? = null
    fun where (block: () -> SqlExpression) {
        whereClause = block()
    }
}

interface SqlExpression {
    fun build(): String
}

class SqlCol (val name: String) : SqlExpression {
    override fun build() = name
}

class SqlValStr (val value: String) : SqlExpression {
    override fun build() = "'$value'"
}

class SqlValInt (val value: Int) : SqlExpression {
    override fun build() = value.toString()
}

class SqlValNull () : SqlExpression {
    override fun build() = "null"
}

class SqlEq (val c: SqlCol, val v: SqlExpression) : SqlExpression {
    override fun build() = if (v is SqlValNull) "${c.build()} is ${v.build()}"
    else "${c.build()} = ${v.build()}"
}

class SqlNonEq(val c: SqlCol, val v: SqlExpression) : SqlExpression {
    override fun build() = if (v is SqlValNull) "${c.build()} !is ${v.build()}"
    else "${c.build()} != ${v.build()}"
}

class SqlOr(val exps: List<SqlExpression>) : SqlExpression {
    override fun build(): String = exps.joinToString (" or ", "(", ")") { it.build() }
}

infix fun <T> String.eq(v: T?): SqlExpression {
    return SqlEq(SqlCol(this), getExpFromValue(v))
}

infix fun <T> String.nonEq (v: T?) : SqlExpression {
    return SqlNonEq (SqlCol(this), getExpFromValue(v))
}

private fun <T> getExpFromValue (v: T?) =  when {
    v == null -> SqlValNull()
    v is Int -> SqlValInt (v)
    else -> SqlValStr(v.toString())
}

fun query (block: SqlSelectBuilder.() -> Unit) : SqlSelectBuilder {
    return SqlSelectBuilder().apply(block)
}

fun or (vararg args: SqlExpression): SqlExpression =  SqlOr(args.toList())

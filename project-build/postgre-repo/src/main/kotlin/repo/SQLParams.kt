package repo

data class SQLParams(
    val url: String = "localhost",
    val user: String = "postgres",
    val password: String = "mysecretpassword",
    val schema: String = "public"
) {
    constructor(
        host: String = "localhost",
        port: Int = 5432,
        user: String = "postgres",
        password: String = "mysecretpassword",
        database: String = "postgres",
        schema: String = "public",
    ): this("jdbc:postgresql://${host}:${port}/${database}", user, password, schema)

}
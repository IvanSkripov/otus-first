package ru.otus.kotlin.course.app.spring.config

import io.klogging.Klogging
import io.klogging.NoCoLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import repo.SQLParams

// @Component
@ConfigurationProperties (prefix = "psql")
class SpringSQLParams (
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "",
    var database: String = "postgres",
    var schema: String = "public",
) {

    fun asSQLParam(): SQLParams =  SQLParams(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema
    ).also { logger.debug("Loaded SQL Params",  mapOf("SQLParams" to it)) }
    companion object: NoCoLogging {

    }
}
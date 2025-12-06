package ru.otus.kotlin.course.app.spring.config

import io.klogging.Klogging
import io.klogging.NoCoLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import repo.SQLParams

// @Component
@ConfigurationProperties (prefix = "psql")
class SpringSQLParams (
    var url: String = "localhost",
    var user: String = "postgres",
    var password: String = "",
    var schema: String = "public",
) {

    fun asSQLParam(): SQLParams =  SQLParams(
        url = url,
        user = user,
        password = password,
        schema = schema
    ).also { logger.debug("Loaded SQL Params",  mapOf("SQLParams" to it)) }
    companion object: NoCoLogging {

    }
}
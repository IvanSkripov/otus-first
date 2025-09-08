package ru.otus.kotlin.course.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.api.v1.models.IResponse

val apiMapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    addModule(kotlinModule())
    build()
}

fun apiRequestSerialize(request: IRequest): String
            = apiMapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T: IRequest > apiRequestDeserialize(json: String) : T =
    apiMapper.readValue(json, IRequest::class.java) as T

fun apiResponseSerialize(response: IResponse): String
                = apiMapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T: IResponse> apiResponseDeserialize (json: String): T =
        apiMapper.readValue(json, IResponse::class.java) as T
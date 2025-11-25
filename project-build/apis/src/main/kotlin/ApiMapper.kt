package ru.otus.kotlin.course.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import ru.otus.kotlin.course.api.v1.models.IRequest
import ru.otus.kotlin.course.api.v1.models.IResponse
import ru.otus.kotlin.course.api.v1.models.ImageCreateRequest

val apiMapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    addModule(kotlinModule())
    build()
}

data class CreateRequest (val req: ImageCreateRequest, val file: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateRequest

        if (req != other.req) return false
        if (!file.contentEquals(other.file)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = req.hashCode()
        result = 31 * result + file.contentHashCode()
        return result
    }
}

// Format = len (4 bytes) + json + file data
fun apiBytesToCreateRequest(bytes: ByteArray): CreateRequest {
    val length = bytes[0].toUByte().toInt()
    val reqStr = String(bytes, 1, length.toInt(), Charsets.UTF_8)
    val req = apiMapper.readValue(reqStr, ImageCreateRequest::class.java)
    return CreateRequest(req, bytes.copyOfRange(length.toInt()+1, bytes.size))
}

fun apiCreateRequestToBytes(cr: CreateRequest): ByteArray {
    val str = apiMapper.writeValueAsString(cr.req)
    require (str.length < 255)
    var ba =  byteArrayOf(str.length.toByte()) + str.toByteArray(Charsets.UTF_8)
    ba += cr.file
    return ba
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
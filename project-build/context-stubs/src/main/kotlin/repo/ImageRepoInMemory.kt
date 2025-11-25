package ru.otus.kotlin.course.common.stubs.repo

import io.github.reactivecircus.cache4k.Cache
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.common.stubs.getDefaultId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.kotlin.course.common.models.PsImageId

class ImageRepoInMemory (
    ttl: Duration = 2.minutes,
    val randomId: () -> String = { getDefaultId() }
): ImageRepoBase() {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, ImageEntity>()
        .expireAfterWrite(ttl)
        .build()

    override suspend fun createImage(req: DBImageRequest): IDBResult = tryRun {
        val key = randomId()
        val ret = req.image.copy(id = PsImageId(key))
        val entity = ImageEntity(req.image)
        mutex.withLock {
            cache.put(key, entity)
        }
        DBGetImage(ret)
    }

    override suspend fun readImage(id: DBImageId): IDBResult = tryRun  {
        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {  
                   DBGetImage(it.toModel())
                } ?: errorNotFound (key)

        }
    }

    override suspend fun updateImage(req: DBImageRequest): IDBResult {
        TODO("Not yet implemented")
    }

    override suspend fun deleteImage(id: DBImageRequest): IDBResult {
        TODO("Not yet implemented")
    }

    override suspend fun searchImages(criteria: DBImageSearchFilter): IDBResult {
        TODO("Not yet implemented")
    }
}
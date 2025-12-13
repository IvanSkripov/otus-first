package ru.otus.kotlin.course.common.stubs.repo

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import ru.otus.kotlin.course.common.repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId

class ImageRepoInMemory (
    ttl: Duration = 2.minutes,
    val randomId: () -> String = { uuid4().toString() },
    val updateLogic: (old: ImageEntity?, new: ImageEntity) -> Unit = ::updateEntity
): ImageRepoBase(), IRepoInitializer {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, ImageEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(objects: Collection<PsImage>): Collection<PsImage> = objects.map {
        require(it.id != PsImageId.NONE)
        val key = it.id.asString()
        val entity = ImageEntity(it)
        cache.put(key, entity)
        it
    }

    override suspend fun createImage(req: DBImageRequest): IDBResult = tryRun {
        val key = randomId()
        val ret = req.image.copy(id = PsImageId(key))
        val entity = ImageEntity(ret)
        entity.bytes = req.image.file.copyOf()
        mutex.withLock {
            cache.put(key, entity)
        }
        DBGetImage(ret)
    }

    override suspend fun readImage(id: DBImageId, withData: Boolean): IDBResult = tryRun  {
        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {  
                   DBGetImage(it.toModel())
                } ?: errorNotFound (key)

        }
    }

    override suspend fun updateImage(req: DBImageRequest): IDBResult = tryRun {
        val key = req.image.id.takeIf { it != PsImageId.NONE }?.asString() ?: return@tryRun errorEmptyId
        val entity = ImageEntity(req.image)
        mutex.withLock {
            val old = cache.get(key)
            when {
                old == null -> errorNotFound(key)
                else -> {
                    updateLogic(old, entity)
                    cache.put(key, entity)
                    DBGetImage(req.image)
                }
            }
        }

    }

    override suspend fun deleteImage(id: DBImageId): IDBResult = tryRun {
        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId
        mutex.withLock {
            val old = cache.get(key)
            when {
                old == null -> errorNotFound(key)
                else -> {
                    cache.invalidate(key)
                    DBGetImage(old.toModel())
                }
            }
        }
    }

    override suspend fun searchImages(criteria: DBImageSearchFilter): IDBResult = tryRun {
        val list = mutableListOf<PsImage>()
        mutex.withLock {
            val keys = cache.asMap().filterValues {
                it.title.contains(criteria.asString())
            }.map { (key, value) ->
                list.add(value.toModel())
            }
        }
        DBGetImages(list)
    }

}

fun updateEntity(old: ImageEntity?, new: ImageEntity) {
    if (new.imageUrl.isEmpty()) { new.imageUrl = old?.imageUrl ?: "" }
    if (new.previewUrl.isEmpty()) { new.previewUrl = old?.previewUrl ?: "" }
    if (new.permanentLinkUrl.isEmpty()) { new.permanentLinkUrl = old?.permanentLinkUrl ?: "" }

    if (new.bytes.size == 0) {
        new.bytes = old?.bytes?.copyOf() ?: ByteArray(0)
    }
}

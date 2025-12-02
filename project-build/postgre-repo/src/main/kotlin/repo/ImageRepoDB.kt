package ru.otus.kotlin.course.common.stubs.repo.postgre

import com.benasher44.uuid.uuid4
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.repo.*


class ImageRepoDB (
    val randomId: () -> String = { uuid4().toString() },
): ImageRepoBase(), IRepoInitializer {

    override fun save(objects: Collection<PsImage>): Collection<PsImage> = objects.map {
        require(it.id != PsImageId.NONE)
        val key = it.id.asString()

        it
    }

    override suspend fun createImage(req: DBImageRequest): IDBResult = tryRun {
        TODO("Not yet implemented")
    }
//        val key = randomId()
//        val ret = req.image.copy(id = PsImageId(key))
//        val entity = ImageEntity(ret)
//        entity.bytes = req.image.file.copyOf()
////        mutex.withLock {
////            cache.put(key, entity)
////        }
////        DBGetImage(ret)
//    }

    override suspend fun readImage(id: DBImageId): IDBResult = tryRun  {
        TODO("Not yet implemented")
//        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId
//        mutex.withLock {
//            cache.get(key)
//                ?.let {
//                   DBGetImage(it.toModel())
//                } ?: errorNotFound (key)
//
//        }
    }

    override suspend fun updateImage(req: DBImageRequest): IDBResult = tryRun {
        TODO("Not yet implemented")
//        val key = req.image.id.takeIf { it != PsImageId.NONE }?.asString() ?: return@tryRun errorEmptyId
//        val entity = ImageEntity(req.image)
//        mutex.withLock {
//            val old = cache.get(key)
//            when {
//                old == null -> errorNotFound(key)
//                else -> {
//                    updateLogic(old, entity)
//                    cache.put(key, entity)
//                    DBGetImage(req.image)
//                }
//            }
//        }

    }

    override suspend fun deleteImage(id: DBImageId): IDBResult = tryRun {
        TODO("Not yet implemented")
        //val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId
//        mutex.withLock {
//            val old = cache.get(key)
//            when {
//                old == null -> errorNotFound(key)
//                else -> {
//                    cache.invalidate(key)
//                    DBGetImage(old.toModel())
//                }
//            }
//        }
    }

    override suspend fun searchImages(criteria: DBImageSearchFilter): IDBResult {
        TODO("Not yet implemented")
    }

}

//fun updateEntity(old: ImageEntity?, new: ImageEntity) {
//    if (new.imageUrl.isEmpty()) { new.imageUrl = old?.imageUrl ?: "" }
//    if (new.previewUrl.isEmpty()) { new.previewUrl = old?.previewUrl ?: "" }
//    if (new.permanentLinkUrl.isEmpty()) { new.permanentLinkUrl = old?.permanentLinkUrl ?: "" }
//
//    if (new.bytes.size == 0) {
//        new.bytes = old?.bytes?.copyOf() ?: ByteArray(0)
//    }
//}

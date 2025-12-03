package ru.otus.kotlin.course.common.stubs.repo.postgre

import com.benasher44.uuid.uuid4
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import java.sql.DriverManager
import repo.SQLParams
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.repo.postgre.tables.pojos.Images
import ru.otus.kotlin.course.repo.postgre.tables.pojos.LabelValues
import ru.otus.kotlin.course.repo.postgre.tables.pojos.Tags
import ru.otus.kotlin.course.repo.postgre.tables.references.IMAGES
import ru.otus.kotlin.course.repo.postgre.tables.references.LABELS
import ru.otus.kotlin.course.repo.postgre.tables.references.LABEL_VALUES
import ru.otus.kotlin.course.repo.postgre.tables.references.TAGS
import toPsImage
import kotlin.coroutines.Continuation


class ImageRepoDB (
    private val params: SQLParams,
    val randomId: () -> String = { uuid4().toString() },
): ImageRepoBase(), IRepoInitializer {

    private val context: DSLContext = DSL.using(
        DriverManager.getConnection(params.url, params.user, params.password),
        SQLDialect.POSTGRES
    )


    override fun save(objects: Collection<PsImage>): Collection<PsImage> = objects.map {
        require(it.id != PsImageId.NONE)
        val key = it.id.asString()
        insertImage(it)
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
        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId

        //context.transaction { cfg ->
          //  val ctx = DSL.using(cfg)
            val image = context.selectFrom(IMAGES)
                .where(IMAGES.ID.eq(key))
                .fetchOneInto(Images::class.java)

//            val tags = context.selectFrom(TAGS)
//                .where(TAGS.IMAGE_ID.eq(key))
//                .fetchInto(Tags::class.java)

            val tags = context.selectFrom(TAGS)
                    .where(TAGS.IMAGE_ID.eq(key))
                    .fetch { r -> r[TAGS.VALUE] }

//        return context.select(
//            USERS.ID.`as`("userId"),
//            USERS.EMAIL.`as`("email"),
//            ORDERS.ID.`as`("orderId"),
//            ORDERS.TOTAL.`as`("total"),
//        )
//            .from(USERS)
//            .leftJoin(ORDERS).on(ORDERS.USER_ID.eq(USERS.ID))
//            .fetchInto(UserOrderDto::class.java)
//    }

            val labels = context.select(
                LABEL_VALUES.LABEL_KEY, //.`as`("key"),
                LABEL_VALUES.VALUE, //.`as`("value"),
                LABELS.DESCRIPTION //.`as`("desc")
                )
                .from(LABEL_VALUES)
                .leftJoin(LABELS).on(LABELS.KEY.eq(LABEL_VALUES.LABEL_KEY))
                .where(LABEL_VALUES.IMAGE_ID.eq(key))
                .fetch() {  r ->
                    PsLabel(
                        key = r.get(LABEL_VALUES.LABEL_KEY) ?: "",
                        value = r.get(LABEL_VALUES.VALUE) ?: "",
                        desc = r.get(LABELS.DESCRIPTION) ?: ""
                    )

                }



            image?.let {
                 DBGetImage(image.toPsImage(tags, labels))
            }  ?:  errorNotFound (key)
        //}

    TODO("Not yet implemented")
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

    private fun insertImage(image: PsImage) {
        context.transaction { cfg ->
            val ctx = DSL.using(cfg)
            val imageId = ctx.insertInto(
                IMAGES,
                IMAGES.ID,
                IMAGES.TITLE,
                IMAGES.DESCRIPTION,
                IMAGES.IMAGEURL,
                IMAGES.PREVIEWURL,
                IMAGES.PERMANENTLINKURL)
                .values(
                    image.id.asString(),
                    image.title,
                    image.desc,
                    image.imageUrl,
                    image.previewUrl,
                    image.permanentLinkUrl)
                .returning(IMAGES.ID)
                .fetchOne()!!
                .id

            image.tags.forEach {
                ctx.insertInto(
                    TAGS,
                    TAGS.ID,
                    TAGS.IMAGE_ID,
                    TAGS.VALUE)
                    .values(
                        uuid4().toString(),
                        imageId,
                        it)
                    .execute()
            }

            image.labels.forEach {
                ctx.insertInto(
                    LABEL_VALUES,
                    LABEL_VALUES.LABEL_KEY,
                    LABEL_VALUES.IMAGE_ID,
                    LABEL_VALUES.VALUE)
                    .values(
                        it.key,
                        imageId,
                        it.value)
                    .execute()
            }
        }
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

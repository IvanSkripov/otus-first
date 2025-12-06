package ru.otus.kotlin.course.common.stubs.repo.postgre

import com.benasher44.uuid.uuid4
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.DriverManager
import repo.SQLParams
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.repo.postgre.tables.pojos.Images
import ru.otus.kotlin.course.repo.postgre.tables.references.*


import toPsImage


class ImageRepoDB(
    private val params: SQLParams,
    val randomId: () -> String = { uuid4().toString() },
) : ImageRepoBase(), IRepoInitializer {

    private val context: DSLContext by lazy {
        DSL.using(
            DriverManager.getConnection(params.url, params.user, params.password),
            SQLDialect.POSTGRES
        )
    }

    override fun save(objects: Collection<PsImage>): Collection<PsImage> = objects.map {
        require(it.id != PsImageId.NONE)
        context.transaction { cfg ->
            val ctx = DSL.using(cfg)
            upsertImage(ctx,it)
        }
        it
    }

    override suspend fun createImage(req: DBImageRequest): IDBResult = tryRun {
        val key = randomId()
        val ret = req.image.copy(id = PsImageId(key))

        context.transaction { cfg ->
            val ctx = DSL.using(cfg)
            upsertImage(ctx, ret)
            ctx.insertInto(FILES, FILES.IMAGE_ID, FILES.DATA)
                .values(key, req.image.file)
                .execute()
        }
        DBGetImage(ret)

    }

    override suspend fun readImage(id: DBImageId, withData: Boolean): IDBResult = tryRun {
        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId

        var ret: IDBResult = errorNotFound(key)
        context.transaction { cfg ->
            val ctx = DSL.using(cfg)
            val image = readImageFullEntity(ctx, key, withData)
            image?.let {
                ret = DBGetImage(image)
            }
        }

        ret
    }

    override suspend fun updateImage(req: DBImageRequest): IDBResult = tryRun {
        val key = req.image.id.takeIf { it != PsImageId.NONE }?.asString() ?: return@tryRun errorEmptyId

        var ret: IDBResult = errorNotFound(key)
        context.transaction { cfg ->
            val ctx = DSL.using(cfg)

            val i = ctx.selectFrom(IMAGES)
                .where(IMAGES.ID.eq(req.image.id.asString()))
                .fetchOneInto(Images::class.java)

            if (i == null) return@transaction

            upsertImage(ctx, req.image)

            ret = DBGetImage(req.image)
        }
        ret
    }

    override suspend fun deleteImage(id: DBImageId): IDBResult = tryRun {
        val key = id.takeIf { !it.isEmpty() }?.asString() ?: return@tryRun errorEmptyId
        var ret: IDBResult = errorNotFound(key)
        context.transaction { cfg ->
            val ctx = DSL.using(cfg)
            val old = readImageFullEntity(ctx, key, false)

            ctx.deleteFrom(IMAGES).where(IMAGES.ID.eq(key)).execute()
            ctx.deleteFrom(TAGS).where(TAGS.IMAGE_ID.eq(key)).execute()
            ctx.deleteFrom(LABEL_VALUES).where(LABEL_VALUES.IMAGE_ID.eq(key)).execute()
            ctx.deleteFrom(FILES).where(FILES.IMAGE_ID.eq(key)).execute()

            old?.let {
                ret = DBGetImage(old)
            }
        }
        ret
    }

    override suspend fun searchImages(criteria: DBImageSearchFilter): IDBResult = tryRun {
        val list = mutableListOf<PsImage>()
        context.transaction { cfg ->
            val ctx = DSL.using(cfg)

            val keys = ctx.select(IMAGES.ID)
                .from(IMAGES)
                .where(IMAGES.TITLE.like("%${criteria.asString()}%"))
                .fetch {r -> r[IMAGES.ID] }

            keys.forEach() { key ->
                val image = readImageFullEntity(ctx, key, false)
                image?.let {  list.add(it) }
            }
        }
        DBGetImages(list)
    }

    private fun upsertImage(ctx: DSLContext, image: PsImage) {

        val imageId = ctx.insertInto(
            IMAGES,
            IMAGES.ID,
            IMAGES.TITLE,
            IMAGES.DESCRIPTION,
            IMAGES.IMAGEURL,
            IMAGES.PREVIEWURL,
            IMAGES.PERMANENTLINKURL
        )
            .values(
                image.id.asString(),
                image.title,
                image.desc,
                image.imageUrl,
                image.previewUrl,
                image.permanentLinkUrl
            )
            .onConflict(IMAGES.ID)
            .doUpdate()
            .set(IMAGES.TITLE, image.title)
            .set(IMAGES.DESCRIPTION, image.desc)
            .set(IMAGES.IMAGEURL, image.imageUrl)
            .set(IMAGES.PREVIEWURL, image.previewUrl)
            .set(IMAGES.PERMANENTLINKURL, image.permanentLinkUrl)
            .returning(IMAGES.ID)
            .fetchOne()!!
            .id

        ctx.deleteFrom(TAGS).where(TAGS.IMAGE_ID.eq(imageId)).execute()

        image.tags.forEach {
            ctx.insertInto(
                TAGS,
                TAGS.ID,
                TAGS.IMAGE_ID,
                TAGS.VALUE
            )
                .values(
                    uuid4().toString(),
                    imageId,
                    it
                )
                .execute()
        }

        ctx.deleteFrom(LABEL_VALUES).where(LABEL_VALUES.IMAGE_ID.eq(imageId)).execute()

        image.labels.forEach {
            ctx.insertInto(
                LABEL_VALUES,
                LABEL_VALUES.LABEL_KEY,
                LABEL_VALUES.IMAGE_ID,
                LABEL_VALUES.VALUE
            )
                .values(
                    it.key,
                    imageId,
                    it.value
                )
                .execute()
        }
    }

    private fun readImageFullEntity(ctx: DSLContext, key: String, withData: Boolean): PsImage? {
        val image = ctx.selectFrom(IMAGES)
            .where(IMAGES.ID.eq(key))
            .fetchOneInto(Images::class.java)

        val tags = ctx.selectFrom(TAGS)
            .where(TAGS.IMAGE_ID.eq(key))
            .fetch { r -> r[TAGS.VALUE] }

        val labels = ctx.select(
            LABEL_VALUES.LABEL_KEY,
            LABEL_VALUES.VALUE,
            LABELS.DESCRIPTION
        )
            .from(LABEL_VALUES)
            .leftJoin(LABELS).on(LABELS.KEY.eq(LABEL_VALUES.LABEL_KEY))
            .where(LABEL_VALUES.IMAGE_ID.eq(key))
            .fetch() { r ->
                PsLabel(
                    key = r.get(LABEL_VALUES.LABEL_KEY) ?: "",
                    value = r.get(LABEL_VALUES.VALUE) ?: "",
                    desc = r.get(LABELS.DESCRIPTION) ?: ""
                )
            }


        val data = if (withData) {
            ctx.select(FILES.DATA).from(FILES).where(FILES.IMAGE_ID.eq(key)).fetchOne(FILES.DATA) ?: ByteArray(0)
        } else {
            ByteArray(0)
        }


        return image?.let {
            image.toPsImage(tags, labels, data)
        }
    }
}
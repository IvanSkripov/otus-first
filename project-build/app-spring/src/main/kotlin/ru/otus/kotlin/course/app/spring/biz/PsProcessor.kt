package ru.otus.kotlin.course.app.spring.biz

import io.klogging.Klogging
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel
import ru.otus.kotlin.course.common.stubs.*
import ru.otus.kotlin.course.common.worker.IPsProcessor
import ru.otus.kotlin.course.cor.rootChain

class PsProcessor(
    private val corSettings: PsCoreSettings = PsCoreSettings.NONE
) : IPsProcessor, Klogging {
    override suspend fun exec(ctx: PsBeContext) = businessChain.build().exec(ctx)

    val businessChain = rootChain<PsBeContext> {
        initStatus("Инициализируем статус")

        operation("Обработка Create", PsCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateOk("Ok")
                stubWrongImageSize("Неверный размер изображения")
                stubWrongImageFormat("Неверный формат изображения")
                stubWrongLink("Неверная ссылка на изображение")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                validateTitleNotEmpty("Проверка заголовка")
                endValidation("Завершение проверок")
            }
        }
        operation("Обработка READ", PsCommand.READ) {
            stubs("Обработка стабов") {
                stubReadOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubWrongLink("Неверная ссылка на изображение")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                endValidation("Завершение проверок")
            }
        }
        operation("Обработка UPDATE", PsCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubWrongLink("Неверная ссылка на изображение")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                validateTitleNotEmpty("Проверка заголовка")
                endValidation("Завершение проверок")
            }
        }

        operation("Обработка DOWNLOAD", PsCommand.DOWNLOAD) {
            stubs("Обработка стабов") {
                stubDownloadOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                endValidation("Завершение проверок")
            }
        }
        operation("Обработка LINK", PsCommand.LINK) {
            stubs("Обработка стабов") {
                stubLinkOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                endValidation("Завершение проверок")
            }
        }
        operation("Обработка SEARCH", PsCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateSearchStringNotEmpty("Проверка поискового критерия")
                endValidation("Завершение проверок")
            }
        }

        operation("Обработка DELETE", PsCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                endValidation("Завершение проверок")
            }
        }



        operation("Обработка Ws.Init", PsCommand.INIT) {
            logAndExit("WS Init processing", corSettings)
        }
        operation("Обработка Ws.Finished", PsCommand.FINISHED) {
            logAndExit("WS Finished processing", corSettings)
        }
    }
}


//// TODO: Generate real link
//private val PERMANENT_URL = "www.otus-first.ru/permanent/"
//private val PREVIEW_URL = "www.otus-first.ru/preview/"
//private val IMAGE_URL = "www.otus-first.ru/image/"
//
//private suspend fun execLogic(ctx: PsBeContext) {
//    ctx.state = PsState.RUNNING
//    when(ctx.command) {
//        PsCommand.CREATE -> {
//            ctx.request.imageUrl = "${IMAGE_URL}${uuid4().toString()}"
//            ctx.request.previewUrl = "${PREVIEW_URL}${uuid4().toString()}"
//            val res = ctx.imageRepo.createImage(DBImageRequest(ctx.request))
//            resultUpdateContext(ctx, res)
//        }
//        PsCommand.READ -> {
//            val res = ctx.imageRepo.readImage(ctx.request.id.toDB() )
//            resultUpdateContext(ctx, res)
//        }
//        PsCommand.DOWNLOAD -> {
//            val res = ctx.imageRepo.readImage(ctx.request.id.toDB(), true)
//            resultUpdateContext(ctx, res)
//        }
//        PsCommand.LINK -> {
//            val res = ctx.imageRepo.readImage(ctx.request.id.toDB())
//            val old = getResultIfPositive(res)
//            if (old != null) {
//                old.permanentLinkUrl = "${PERMANENT_URL}${uuid4().toString()}"
//                val res = ctx.imageRepo.updateImage(DBImageRequest(old),)
//                resultUpdateContext(ctx, res)
//            }
//        }
//        PsCommand.DELETE -> {
//            val res = ctx.imageRepo.deleteImage(ctx.request.id.toDB())
//            resultUpdateContext(ctx, res)
//        }
//        PsCommand.UPDATE -> {
//            val res = ctx.imageRepo.updateImage(DBImageRequest(ctx.request))
//            resultUpdateContext(ctx, res)
//        }
//        PsCommand.SEARCH -> {
//            val res = ctx.imageRepo.searchImages(DBImageSearchFilter(ctx.filterString))
//            resultUpdateContext(ctx, res)
//        }
//        else -> TODO("Not implemented")
//    }
//}
//
//
//private fun resultUpdateContext(ctx: PsBeContext, res: IDBResult) {
//    when (res) {
//        is DBGetImage -> ctx.response = res.image
//        is DBGetImages -> ctx.responseList = res.images.toMutableList()
//        is DBError -> {
//            ctx.errors.add(res.asPsError())
//            ctx.state = PsState.FAILING
//        }
//    }
//}
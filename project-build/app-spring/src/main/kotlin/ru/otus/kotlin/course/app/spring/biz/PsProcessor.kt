package ru.otus.kotlin.course.app.spring.biz

import io.klogging.Klogging
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.models.PsLabel
import ru.otus.kotlin.course.common.stubs.*
import ru.otus.kotlin.course.common.worker.IPsProcessor
import ru.otus.kotlin.course.cor.chain
import ru.otus.kotlin.course.cor.rootChain

class PsProcessor(
    private val corSettings: PsCoreSettings = PsCoreSettings.NONE
) : IPsProcessor, Klogging {
    override suspend fun exec(ctx: PsBeContext) = businessChain.build().exec(ctx)

    val businessChain = rootChain<PsBeContext> {
        initStatus("Инициализируем статус")
        initRepo ("Инициализируем репозитории", corSettings)

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

            chain {
                repoCreate("Создаем изображение")
                prepareResult("Обрабатываем результат")
            }

        }
        operation("Обработка READ", PsCommand.READ) {
            stubs("Обработка стабов") {
                stubReadOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                endValidation("Завершение проверок")
            }

            chain {
                repoRead("Читаем изображение")
                prepareResult("Обрабатываем результат")
            }
        }
        operation("Обработка UPDATE", PsCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateOk("Ok")
                stubWrongOwner("Пользователь не является владельцем изображения")
                stubDbError("Ошибка в БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                validateId("Проверка Id")
                validateTitleNotEmpty("Проверка заголовка")
                endValidation("Завершение проверок")
            }
            chain {
                repoUpdate("Обновляем изображение")
                prepareResult("Обрабатываем результат")
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

            chain {
                repoDownload("Читаем изображение и скачиваем ")
                prepareResult("Обрабатываем результат")
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

            chain {
                repoLink("Получаем постоянную ссылку")
                prepareResult("Обрабатываем результат")
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

            chain {
                repoSearch("Ищем изображения")
                prepareResult("Обрабатываем результат")
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
            chain {
                repoDelete("Удаляем изображение")
                prepareResult("Обрабатываем результат")
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





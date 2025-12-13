package ru.otus.kotlin.course.app.spring.biz

import com.benasher44.uuid.uuid4
import io.klogging.logger
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.models.PsWorkMode
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.cor.ICorChainDsl
import ru.otus.kotlin.course.cor.chain
import ru.otus.kotlin.course.cor.worker

//// TODO: Generate real link
private val PERMANENT_URL = "www.otus-first.ru/permanent/"
private val PREVIEW_URL = "www.otus-first.ru/preview/"
private val IMAGE_URL = "www.otus-first.ru/image/"

fun  ICorChainDsl<PsBeContext>.operation (title: String, command: PsCommand, block: ICorChainDsl<PsBeContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.state == PsState.RUNNING && this.command == command }
}

fun  ICorChainDsl<PsBeContext>.stubs (title: String = "Обработка стабов", block: ICorChainDsl<PsBeContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.state == PsState.RUNNING && this.workMode == PsWorkMode.STUB }
}

fun  ICorChainDsl<PsBeContext>.validation (title: String = "Валидация", block: ICorChainDsl<PsBeContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.state == PsState.RUNNING && this.workMode != PsWorkMode.STUB }
}

fun  ICorChainDsl<PsBeContext>.initStatus (title: String = "Инициализируем статус") = worker {
    this.title = title
    this.description = "Prepare PsBeContext"
    on { this.state == PsState.NONE  }
    handle { this.state = PsState.RUNNING }
}

fun  ICorChainDsl<PsBeContext>.initRepo (title: String = "Инициализируем репозиторий", coreSettings: PsCoreSettings) = worker {
    this.title = title
    this.description = "Prepare Repositories"
    on { this.state == PsState.RUNNING && workMode != PsWorkMode.STUB  }
    handle {
        imageRepo = when(workMode) {
            PsWorkMode.TEST -> coreSettings.repoTest
            PsWorkMode.PROD -> coreSettings.repoProd
            else -> IImageRepo.NONE
        }
        println ("${this}")
    }
}

fun  ICorChainDsl<PsBeContext>.logAndExit (message: String, coreSettings: PsCoreSettings) = worker {
    this.title = "Логируем сообщение и выходим"
    this.description = "Log info and Exit"
    on { this.state == PsState.RUNNING  }
    handle {
        val logger = coreSettings.loggerProvider.logger("logAndExit")
        logger.info(message)
        this.state == PsState.FINISHING
    }
}

fun  ICorChainDsl<PsBeContext>.repoCreate (message: String) = worker {
    this.title = "Создаем запись в БД"
    this.description = "Create Image"
    on { this.state == PsState.RUNNING  }
    handle {
        request.imageUrl = "${IMAGE_URL}${uuid4().toString()}"
        request.previewUrl = "${PREVIEW_URL}${uuid4().toString()}"
        dbResponse = imageRepo.createImage(DBImageRequest(request))
    }
}

fun  ICorChainDsl<PsBeContext>.repoRead (message: String) = worker {
    this.title = "Чатаем запись из БД"
    this.description = "Read Image"
    on { this.state == PsState.RUNNING  }
    handle {
        dbResponse = imageRepo.readImage(request.id.toDB() )
    }
}

fun  ICorChainDsl<PsBeContext>.repoDownload (message: String) = worker {
    this.title = "Скачиваем из БД"
    this.description = "Download Image"
    on { this.state == PsState.RUNNING  }
    handle {
        dbResponse = imageRepo.readImage(request.id.toDB() , true)
    }
}

fun ICorChainDsl<PsBeContext>.repoLink(message: String) = worker {
    this.title = "Получаем ссылку"
    this.description = "Link Image"
    on { this.state == PsState.RUNNING }
    handle {
        dbResponse = imageRepo.readImage(request.id.toDB())
        val old = dbResponse.getResultIfPositive()
        if (old != null) {
            old.permanentLinkUrl = "${PERMANENT_URL}${uuid4().toString()}"
            dbResponse = imageRepo.updateImage(DBImageRequest(old))
        }
    }
}

fun ICorChainDsl<PsBeContext>.repoDelete(message: String) = worker {
    this.title = "Удаляем изображение"
    this.description = "Delete Image"
    on { this.state == PsState.RUNNING }
    handle {
        dbResponse = imageRepo.deleteImage(request.id.toDB())
    }
}

fun ICorChainDsl<PsBeContext>.repoUpdate(message: String) = worker {
    this.title = "Обновляем изображение"
    this.description = "Update Image"
    on { this.state == PsState.RUNNING }
    handle {
        dbResponse = imageRepo.updateImage(DBImageRequest(request))
    }
}

fun ICorChainDsl<PsBeContext>.repoSearch(message: String) = worker {
    this.title = "Ищем изображения"
    this.description = "Search Image"
    on { this.state == PsState.RUNNING }
    handle {
        dbResponse = imageRepo.searchImages(DBImageSearchFilter(filterString))
    }
}

fun ICorChainDsl<PsBeContext>.prepareResult(message: String) = worker {
    this.title = "Обрабатываем результат"
    this.description = "Processing Result"
    on { this.state == PsState.RUNNING }
    handle {
        val res = dbResponse
        when (res) {
            is DBGetImage -> response = res.image
            is DBGetImages -> responseList = res.images.toMutableList()
            is DBError -> {
                errors.add(res.asPsError())
                state = PsState.FAILING
            }
        }
    }
}


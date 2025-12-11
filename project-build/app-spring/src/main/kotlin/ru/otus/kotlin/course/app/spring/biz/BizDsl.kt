package ru.otus.kotlin.course.app.spring.biz

import io.klogging.logger
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.models.PsWorkMode
import ru.otus.kotlin.course.cor.ICorChainDsl
import ru.otus.kotlin.course.cor.chain
import ru.otus.kotlin.course.cor.worker

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

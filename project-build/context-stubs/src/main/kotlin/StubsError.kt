package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.models.PsWorkMode
import ru.otus.kotlin.course.cor.ICorChainDsl
import ru.otus.kotlin.course.cor.worker

fun ICorChainDsl<PsBeContext>.stubWrongLink (title: String) = worker {
    this.title = title
    this.description = "Error: Wrong Link"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.WRONG_LINK && this.state == PsState.RUNNING }
    handle {
        this.errors.add(PsImageStubsItems.WRONG_LINK)
        this.state = PsState.FAILING
    }
}

fun ICorChainDsl<PsBeContext>.stubWrongOwner (title: String) = worker {
    this.title = title
    this.description = "Error: Wrong Owner"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.WRONG_OWNER && this.state == PsState.RUNNING }
    handle {
        this.errors.add(PsImageStubsItems.WRONG_OWNER)
        this.state = PsState.FAILING
    }
}

fun ICorChainDsl<PsBeContext>.stubWrongImageSize (title: String) = worker {
    this.title = title
    this.description = "Error: Wrong Image Size"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.WRONG_IMAGE_SIZE && this.state == PsState.RUNNING }
    handle {
        this.errors.add(PsImageStubsItems.WRONG_IMAGE_SIZE)
        this.state = PsState.FAILING
    }
}

fun ICorChainDsl<PsBeContext>.stubWrongImageFormat (title: String) = worker {
    this.title = title
    this.description = "Error: Wrong Image Format"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.WRONG_IMAGE_FORMAT && this.state == PsState.RUNNING }
    handle {
        this.errors.add(PsImageStubsItems.WRONG_IMAGE_FORMAT)
        this.state = PsState.FAILING
    }
}

fun ICorChainDsl<PsBeContext>.stubDbError (title: String) = worker {
    this.title = title
    this.description = "Error: DB Error"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.DB_ERROR && this.state == PsState.RUNNING }
    handle {
        this.errors.add(PsImageStubsItems.DB_ERROR)
        this.state = PsState.FAILING
    }
}

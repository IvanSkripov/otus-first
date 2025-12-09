package ru.otus.kotlin.course.common.stubs

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.*
import ru.otus.kotlin.course.cor.ICorChainDsl
import ru.otus.kotlin.course.cor.worker

fun ICorChainDsl<PsBeContext>.stubCreateOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        this.response =  PsImage( id = PsImageId(IMAGE_ID))
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubReadOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        this.response =  PsImageStubsItems.FULL_TO_PSIMAGE
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubUpdateOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        this.response =  PsImage(id = PsImageId(IMAGE_ID))
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubLinkOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        this.response =  PsImage(id = PsImageId(IMAGE_ID), permanentLinkUrl = PERM_LINK )
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubDownloadOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        val img = PsImage( id = PsImageId(IMAGE_ID))
        img.file = BYTES
        this.response =  img
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubSearchOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        this.responseList =  mutableListOf(PsImageStubsItems.FULL_TO_PSIMAGE, PsImageStubsItems.FULL_TO_PSIMAGE)
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubDeleteOk (title: String) = worker {
    this.title = title
    this.description = "Stub for Create command with OK"
    on {this.workMode == PsWorkMode.STUB && this.stubCase == PsStubs.SUCCESS && this.state == PsState.RUNNING }
    handle {
        this.response = PsImage(id = PsImageId(IMAGE_ID))
        this.state = PsState.FINISHING
    }
}

fun ICorChainDsl<PsBeContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = "Stub for Case NONE"
    on {this.workMode == PsWorkMode.STUB && this.state == PsState.RUNNING }
    handle {
        this.fail(PsError (
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
        this.state = PsState.FAILING
    }
}

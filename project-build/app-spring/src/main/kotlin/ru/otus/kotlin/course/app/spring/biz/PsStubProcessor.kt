package ru.otus.kotlin.course.app.spring.biz

import io.klogging.Klogging
import io.klogging.logger
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.stubs.getStabImages
import ru.otus.kotlin.course.common.stubs.getStub
import ru.otus.kotlin.course.common.worker.IPsProcessor

class PsStubProcessor() : IPsProcessor, Klogging {
    override suspend fun exec(ctx: PsBeContext) {
        when(ctx.command) {
            PsCommand.INIT -> logger.info("WS Init processing")
            PsCommand.FINISHED -> logger.info("WS Finished processing")
            PsCommand.SEARCH -> ctx.responseList = getStabImages ().toMutableList()
            else -> ctx.response = getStub(ctx)
        }
        ctx.state = PsState.RUNNING
    }
}
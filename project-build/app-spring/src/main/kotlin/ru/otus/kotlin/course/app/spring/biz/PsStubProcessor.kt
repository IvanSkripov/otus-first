package ru.otus.kotlin.course.app.spring.biz

import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.stubs.getStub
import ru.otus.kotlin.course.common.worker.IPsProcessor

class PsStubProcessor() : IPsProcessor {
    override suspend fun exec(ctx: PsBeContext) {
        ctx.response = getStub(ctx)
        ctx.state = PsState.RUNNING
    }
}
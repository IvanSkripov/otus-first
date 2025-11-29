package ru.otus.kotlin.course.app.spring.biz

import io.klogging.Klogging
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.models.PsCommand
import ru.otus.kotlin.course.common.models.PsState
import ru.otus.kotlin.course.common.models.PsWorkMode
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.common.stubs.PsStubs
import ru.otus.kotlin.course.common.stubs.getStubImages
import ru.otus.kotlin.course.common.stubs.getStub
import ru.otus.kotlin.course.common.stubs.getStubError
import ru.otus.kotlin.course.common.worker.IPsProcessor

class PsProcessor() : IPsProcessor, Klogging {
    override suspend fun exec(ctx: PsBeContext) {
        when (ctx.command) {
            PsCommand.INIT -> logger.info("WS Init processing")
            PsCommand.FINISHED -> logger.info("WS Finished processing")
            else -> {
                when (ctx.workMode) {
                    PsWorkMode.STUB -> execStub(ctx)
                    PsWorkMode.TEST -> execTest(ctx)
                    else -> execLogic(ctx)
                }
            }
        }
    }

    private suspend fun execStub(ctx: PsBeContext) {
        require(ctx.stubCase != PsStubs.NONE)

        when (ctx.stubCase) {
            PsStubs.SUCCESS -> {
                when (ctx.command) {
                    PsCommand.SEARCH -> ctx.responseList = getStubImages().toMutableList()
                    else -> ctx.response = getStub(ctx)
                }
                ctx.state = PsState.RUNNING
            }
            else -> getStubError(ctx)
        }
    }

    private suspend fun execTest(ctx: PsBeContext) {
        when(ctx.command) {
            PsCommand.CREATE -> {
                val res = ctx.imageRepo.createImage(DBImageRequest(ctx.request))
                when (res) {
                    is DBGetImage -> ctx.response = res.image
                    is DBError -> ctx.errors.add(res.asPsError())
                }
            }
            PsCommand.READ -> {
                val res = ctx.imageRepo.readImage(ctx.request.id.toDB())
                when (res) {
                    is DBGetImage -> ctx.response = res.image
                    is DBError -> ctx.errors.add(res.asPsError())
                }
            }
            else -> TODO("Not implemented")
        }
    }

    private suspend fun execLogic(ctx: PsBeContext) {
       TODO("Implement PROD LOGIC")
    }
}
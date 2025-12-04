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
import com.benasher44.uuid.uuid4
import ru.otus.kotlin.course.common.models.PsImage

class PsProcessor() : IPsProcessor, Klogging {
    override suspend fun exec(ctx: PsBeContext) {
        when (ctx.command) {
            PsCommand.INIT -> logger.info("WS Init processing")
            PsCommand.FINISHED -> logger.info("WS Finished processing")
            else -> {
                when (ctx.workMode) {
                    PsWorkMode.STUB -> execStub(ctx)
                    else -> execLogic(ctx)
                }
            }
        }
    }

    private fun execStub(ctx: PsBeContext) {
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

    // TODO: Generate real link
    private val PERMANENT_URL = "www.otus-first.ru/permanent/"
    private val PREVIEW_URL = "www.otus-first.ru/preview/"
    private val IMAGE_URL = "www.otus-first.ru/image/"

    private suspend fun execLogic(ctx: PsBeContext) {
        ctx.state = PsState.RUNNING
        when(ctx.command) {
            PsCommand.CREATE -> {
                ctx.request.imageUrl = "${IMAGE_URL}${uuid4().toString()}"
                ctx.request.previewUrl = "${PREVIEW_URL}${uuid4().toString()}"
                val res = ctx.imageRepo.createImage(DBImageRequest(ctx.request))
                resultUpdateContext(ctx, res)
            }
            PsCommand.READ -> {
                val res = ctx.imageRepo.readImage(ctx.request.id.toDB() )
                resultUpdateContext(ctx, res)
            }
            PsCommand.DOWNLOAD -> {
                val res = ctx.imageRepo.readImage(ctx.request.id.toDB(), true)
                resultUpdateContext(ctx, res)
            }
            PsCommand.LINK -> {
                val res = ctx.imageRepo.readImage(ctx.request.id.toDB())
                val old = getResultIfPositive(res)
                if (old != null) {
                    old.permanentLinkUrl = "${PERMANENT_URL}${uuid4().toString()}"
                    val res = ctx.imageRepo.updateImage(DBImageRequest(old),)
                    resultUpdateContext(ctx, res)
                }
            }
            PsCommand.DELETE -> {
                val res = ctx.imageRepo.deleteImage(ctx.request.id.toDB())
                resultUpdateContext(ctx, res)
            }
            PsCommand.UPDATE -> {
                val res = ctx.imageRepo.updateImage(DBImageRequest(ctx.request))
                resultUpdateContext(ctx, res)
            }
            PsCommand.SEARCH -> {
                val res = ctx.imageRepo.searchImages(DBImageSearchFilter(ctx.filterString))
                resultUpdateContext(ctx, res)
            }
            else -> TODO("Not implemented")
        }
    }


    private fun resultUpdateContext(ctx: PsBeContext, res: IDBResult) {
        when (res) {
            is DBGetImage -> ctx.response = res.image
            is DBGetImages -> ctx.responseList = res.images.toMutableList()
            is DBError -> {
                ctx.errors.add(res.asPsError())
                ctx.state = PsState.FAILING
            }
        }
    }

    private fun getResultIfPositive(res: IDBResult): PsImage?  = when(res) {
        is DBGetImage -> res.image
        else -> null
    }
}
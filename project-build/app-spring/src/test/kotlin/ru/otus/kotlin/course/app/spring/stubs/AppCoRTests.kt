package ru.otus.kotlin.course.app.spring.stubs

import org.junit.jupiter.api.Test
import ru.otus.kotlin.course.app.spring.biz.PsProcessor
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.stubs.PsImageStubsItems
import ru.otus.kotlin.course.common.stubs.PsStubs
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.kotlin.course.common.models.*
import kotlin.test.assertTrue


class AppCoRTests {

	val processor = PsProcessor(PsCoreSettings.NONE)

	@Test
	fun createTest() = runTest {
		val ctx = PsBeContext(
			command = PsCommand.CREATE,
			workMode = PsWorkMode.STUB,
			stubCase = PsStubs.SUCCESS,
			request = PsImageStubsItems.SIMPLE_REQUEST
		)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertEquals(PsImageStubsItems.SIMPLE_REQUEST, ctx.response)
		assertEquals(0, ctx.errors.size)

	}

	@Test
	fun readTest() = runTest {

		val ctx = PsBeContext(
				command = PsCommand.READ,
				workMode = PsWorkMode.STUB,
				stubCase = PsStubs.SUCCESS,
				request = PsImageStubsItems.SIMPLE_REQUEST
			)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertEquals(PsImageStubsItems.FULL_TO_PSIMAGE, ctx.response)
		assertEquals(0, ctx.errors.size)

	}

	@Test
	fun updateTest() = runTest {
		val ctx = PsBeContext(
			command = PsCommand.UPDATE,
			workMode = PsWorkMode.STUB,
			stubCase = PsStubs.SUCCESS,
			request = PsImageStubsItems.FULL_FROM_PSIMAGE
		)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertEquals(PsImageStubsItems.SIMPLE_REQUEST, ctx.response)
		assertEquals(0, ctx.errors.size)

	}

	@Test
	fun deleteTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.DELETE,
			workMode = PsWorkMode.STUB,
			stubCase = PsStubs.SUCCESS,
			request = PsImageStubsItems.SIMPLE_REQUEST
		)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertEquals(PsImageStubsItems.SIMPLE_REQUEST, ctx.response)
		assertEquals(0, ctx.errors.size)

	}

	@Test
	fun downloadTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.DOWNLOAD,
			workMode = PsWorkMode.STUB,
			stubCase = PsStubs.SUCCESS,
			request = PsImageStubsItems.SIMPLE_REQUEST
		)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertEquals(3, ctx.response.file.size)
		assertEquals(0, ctx.errors.size)

	}

	@Test
	fun searchTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.SEARCH,
			workMode = PsWorkMode.STUB,
			stubCase = PsStubs.SUCCESS,
			filterString = "Find something"
		)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertEquals(2, ctx.responseList.size)
		assertEquals(PsImageStubsItems.FULL_TO_PSIMAGE, ctx.responseList.get(0))
		assertEquals(PsImageStubsItems.FULL_TO_PSIMAGE, ctx.responseList.get(1))
		assertEquals(0, ctx.errors.size)

	}

	@Test
	fun linkTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.LINK,
			workMode = PsWorkMode.STUB,
			stubCase = PsStubs.SUCCESS,
			request = PsImageStubsItems.SIMPLE_REQUEST
		)

		processor.exec(ctx)
		assertEquals(PsState.FINISHING, ctx.state)
		assertTrue (  !ctx.response.permanentLinkUrl.isBlank())
		assertEquals(0, ctx.errors.size)

	}

}

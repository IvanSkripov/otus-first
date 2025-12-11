package ru.otus.kotlin.course.app.spring.stubs

import org.junit.jupiter.api.Test
import ru.otus.kotlin.course.app.spring.biz.PsProcessor
import ru.otus.kotlin.course.common.PsBeContext
import ru.otus.kotlin.course.common.PsCoreSettings
import ru.otus.kotlin.course.common.stubs.PsImageStubsItems
import ru.otus.kotlin.course.common.stubs.PsStubs
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.kotlin.course.common.helpers.VALIDATION_CODE
import ru.otus.kotlin.course.common.models.*
import kotlin.test.assertTrue

val NO_ID = "id"
val NO_TITLE = "title"
val NO_SEARCH = "filterString"

class AppCoRValTests {

	val processor = PsProcessor(PsCoreSettings.NONE)

	@Test
	fun createTest() = runTest {
		val ctx = PsBeContext(
			command = PsCommand.CREATE,
			workMode = PsWorkMode.TEST,
			request = PsImageStubsItems.SIMPLE_REQUEST
		)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertEquals(NO_TITLE, ctx.errors[0].field)

	}

	@Test
	fun readTest() = runTest {

		val ctx = PsBeContext(
				command = PsCommand.READ,
				workMode = PsWorkMode.TEST,
				request = PsImage()
			)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertEquals(NO_ID, ctx.errors[0].field)

	}

	@Test
	fun updateTest() = runTest {
		val ctx = PsBeContext(
			command = PsCommand.UPDATE,
			workMode = PsWorkMode.TEST,
			request = PsImage()
		)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(2, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertTrue (ctx.errors[1].code.contains(VALIDATION_CODE))
		assertEquals(NO_ID, ctx.errors[0].field)
		assertEquals(NO_TITLE, ctx.errors[1].field)

	}

	@Test
	fun deleteTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.DELETE,
			workMode = PsWorkMode.TEST,
			request = PsImage()
		)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertEquals(NO_ID, ctx.errors[0].field)

	}

	@Test
	fun downloadTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.DOWNLOAD,
			workMode = PsWorkMode.TEST,
			request = PsImage()
		)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertEquals(NO_ID, ctx.errors[0].field)

	}

	@Test
	fun searchTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.SEARCH,
			workMode = PsWorkMode.TEST,
			filterString = ""
		)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertEquals(NO_SEARCH, ctx.errors[0].field)

	}

	@Test
	fun linkTest() = runTest {

		val ctx = PsBeContext(
			command = PsCommand.LINK,
			workMode = PsWorkMode.TEST,
			request = PsImage()
		)

		processor.exec(ctx)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertTrue (ctx.errors[0].code.contains(VALIDATION_CODE))
		assertEquals(NO_ID, ctx.errors[0].field)

	}

}

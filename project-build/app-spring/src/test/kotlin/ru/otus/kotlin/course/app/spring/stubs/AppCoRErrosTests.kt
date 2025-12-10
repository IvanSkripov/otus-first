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


class AppCoRErrosTests {

	val processor = PsProcessor(PsCoreSettings.NONE)
	val VALIDATION_CODE = "validation-stab-no-case"

	@Test
	fun createTest() = runTest {
		makeTest(PsCommand.CREATE, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.CREATE, PsStubs.WRONG_OWNER, VALIDATION_CODE )
		makeTest(PsCommand.CREATE, PsStubs.WRONG_LINK, PsImageStubsItems.WRONG_LINK.code )
		makeTest(PsCommand.CREATE, PsStubs.WRONG_IMAGE_SIZE, PsImageStubsItems.WRONG_IMAGE_SIZE.code )
		makeTest(PsCommand.CREATE, PsStubs.WRONG_IMAGE_FORMAT, PsImageStubsItems.WRONG_IMAGE_FORMAT.code )
		makeTest(PsCommand.CREATE, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )
	}


	@Test
	fun readTest() = runTest {
		makeTest(PsCommand.READ, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.READ, PsStubs.WRONG_OWNER, PsImageStubsItems.WRONG_OWNER.code )
		makeTest(PsCommand.READ, PsStubs.WRONG_LINK, VALIDATION_CODE )
		makeTest(PsCommand.READ, PsStubs.WRONG_IMAGE_SIZE, VALIDATION_CODE )
		makeTest(PsCommand.READ, PsStubs.WRONG_IMAGE_FORMAT, VALIDATION_CODE )
		makeTest(PsCommand.READ, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )
	}

	@Test
	fun updateTest() = runTest {
		makeTest(PsCommand.UPDATE, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.UPDATE, PsStubs.WRONG_OWNER, PsImageStubsItems.WRONG_OWNER.code )
		makeTest(PsCommand.UPDATE, PsStubs.WRONG_LINK, VALIDATION_CODE )
		makeTest(PsCommand.UPDATE, PsStubs.WRONG_IMAGE_SIZE, VALIDATION_CODE )
		makeTest(PsCommand.UPDATE, PsStubs.WRONG_IMAGE_FORMAT, VALIDATION_CODE )
		makeTest(PsCommand.UPDATE, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )

	}

	@Test
	fun deleteTest() = runTest {
		makeTest(PsCommand.DELETE, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.DELETE, PsStubs.WRONG_OWNER, PsImageStubsItems.WRONG_OWNER.code )
		makeTest(PsCommand.DELETE, PsStubs.WRONG_LINK, VALIDATION_CODE )
		makeTest(PsCommand.DELETE, PsStubs.WRONG_IMAGE_SIZE, VALIDATION_CODE )
		makeTest(PsCommand.DELETE, PsStubs.WRONG_IMAGE_FORMAT, VALIDATION_CODE )
		makeTest(PsCommand.DELETE, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )

	}

	@Test
	fun downloadTest() = runTest {
		makeTest(PsCommand.DOWNLOAD, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.DOWNLOAD, PsStubs.WRONG_OWNER, PsImageStubsItems.WRONG_OWNER.code )
		makeTest(PsCommand.DOWNLOAD, PsStubs.WRONG_LINK, VALIDATION_CODE )
		makeTest(PsCommand.DOWNLOAD, PsStubs.WRONG_IMAGE_SIZE, VALIDATION_CODE )
		makeTest(PsCommand.DOWNLOAD, PsStubs.WRONG_IMAGE_FORMAT, VALIDATION_CODE )
		makeTest(PsCommand.DOWNLOAD, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )
	}

	@Test
	fun searchTest() = runTest {
		makeTest(PsCommand.SEARCH, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.SEARCH, PsStubs.WRONG_OWNER, PsImageStubsItems.WRONG_OWNER.code )
		makeTest(PsCommand.SEARCH, PsStubs.WRONG_LINK, VALIDATION_CODE )
		makeTest(PsCommand.SEARCH, PsStubs.WRONG_IMAGE_SIZE, VALIDATION_CODE )
		makeTest(PsCommand.SEARCH, PsStubs.WRONG_IMAGE_FORMAT, VALIDATION_CODE )
		makeTest(PsCommand.SEARCH, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )

	}

	@Test
	fun linkTest() = runTest {
		makeTest(PsCommand.LINK, PsStubs.NONE, VALIDATION_CODE )
		makeTest(PsCommand.LINK, PsStubs.WRONG_OWNER, PsImageStubsItems.WRONG_OWNER.code )
		makeTest(PsCommand.LINK, PsStubs.WRONG_LINK, VALIDATION_CODE )
		makeTest(PsCommand.LINK, PsStubs.WRONG_IMAGE_SIZE, VALIDATION_CODE )
		makeTest(PsCommand.LINK, PsStubs.WRONG_IMAGE_FORMAT, VALIDATION_CODE )
		makeTest(PsCommand.LINK, PsStubs.DB_ERROR, PsImageStubsItems.DB_ERROR.code )

	}


	fun makeTest(command: PsCommand, case: PsStubs, code: String ) = runTest {

		val ctx = PsBeContext(
			command = command,
			workMode = PsWorkMode.STUB,
			stubCase = case,
		)

		processor.exec(ctx)
		assertEquals(command, ctx.command)
		assertEquals(PsState.FAILING, ctx.state)
		assertEquals(1, ctx.errors.size)
		assertEquals(code, ctx.errors[0].code)

	}

}

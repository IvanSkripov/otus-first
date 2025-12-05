package ru.otus.kotlin.course.common.stubs.repo.postgre.test

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import liquibase.Liquibase
import org.junit.Test
import repo.SQLParams
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.common.stubs.PsImageStubsItems
import ru.otus.kotlin.course.common.stubs.getDefaultId
import ru.otus.kotlin.course.common.stubs.repo.postgre.ImageRepoDB
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.minutes




class ImageRepoSeverDBTest() {

    @Test
    fun executeTests () {
        val params = SQLParams(host = "localhost")
        val runner: TestRunner = TestRunner(params)

        runner.createImageTest()
        runner.readImageTest()
        runner.updateImageTest()
        runner.deleteImageTest()
        runner.searchImagesTest()
    }
}
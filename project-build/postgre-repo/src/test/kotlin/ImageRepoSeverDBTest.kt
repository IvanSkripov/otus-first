package ru.otus.kotlin.course.common.stubs.repo.postgre.test


import org.junit.Test
import repo.SQLParams


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
package ru.otus.kotlin.course.common.stubs.repo.postgre.test


import org.junit.Test
import repo.SQLParams


class ImageRepoSeverDBTest() {

    @Test
    fun executeTests () {
        val params = SQLParams(host = "localhost")
        val runner: TestRunner = TestRunner(params)

        println("Test: create")
        runner.createImageTest()
        println("Test: read")
        runner.readImageTest()
        println("Test: update")
        runner.updateImageTest()
        println("Test: delete")
        runner.deleteImageTest()
        println("Test: search")
        runner.searchImagesTest()
    }
}
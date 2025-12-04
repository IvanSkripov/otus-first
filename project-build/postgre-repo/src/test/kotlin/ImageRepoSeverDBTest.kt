package ru.otus.kotlin.course.common.stubs.repo.postgre.test

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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

    private fun initRepo (repo: ImageRepoDB, objects: List<PsImage>)
        = RepoInitializer(repo, objects )

    private fun runRepoTest(testRun: suspend TestScope.() -> Unit) = runTest(timeout = 2.minutes) {
        withContext(Dispatchers.Default) {
            testRun()
        }
    }

    val params = SQLParams(host = "localhost")

    @Test
    fun readImageTest () {
        val repo = ImageRepoDB( params = params,  randomId = { getDefaultId() } )
        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
        runRepoTest {
            val res = repo.readImage(DBImageId(getDefaultId()))
            assertIs<DBGetImage>(res)
            val obj = res.image
            assertEquals<PsImage>(PsImageStubsItems.FULL_TO_PSIMAGE, obj)
            // abnormal case test in deleteImageTest
        }
    }

    @Test
    fun updateImageTest () {
        val repo = ImageRepoDB( params = params,  randomId = { getDefaultId() } )
        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
        runRepoTest {
            val image = PsImageStubsItems.FULL_TO_PSIMAGE.copy(imageUrl = "www.kremlin.ru")
            repo.updateImage(DBImageRequest( image),)
            val res = repo.readImage(DBImageId(getDefaultId()))
            assertIs<DBGetImage>(res)
            val obj = res.image
            assertEquals<PsImage>(image, obj)

            // wrong ID
            val WRONG_ID = "321"
            val wrongImage = PsImageStubsItems.FULL_TO_PSIMAGE.copy(id = PsImageId(WRONG_ID))
            val notRes = repo.updateImage(DBImageRequest( wrongImage))
            assertIs<DBError>(notRes)
            assertEquals(errorNotFound(WRONG_ID), notRes)
        }
    }

    @Test
    fun createImageTest () {
        val repo = ImageRepoDB( params = params,  randomId = { getDefaultId() } )
        initRepo(repo, emptyList())
        runRepoTest {
            val cr = repo.createImage(DBImageRequest( PsImageStubsItems.FULL_TO_PSIMAGE))
            assertIs<DBGetImage>(cr)
            val res = repo.readImage(DBImageId(cr.image.id.asString()))
            assertIs<DBGetImage>(res)
            val obj = res.image
            val image = PsImageStubsItems.FULL_TO_PSIMAGE.copy(id = cr.image.id )
            assertEquals<PsImage>(image, obj)
        }
    }

    @Test
    fun deleteImageTest () {
        val repo = ImageRepoDB( params = params,  randomId = { getDefaultId() } )
        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
        runRepoTest {
            val res = repo.deleteImage(DBImageId(getDefaultId()))
            assertIs<DBGetImage>(res)
            val obj = res.image
            assertEquals<PsImage>(PsImageStubsItems.FULL_TO_PSIMAGE, obj)
            // try delete again - faile
            val deleteRes = repo.deleteImage(DBImageId(getDefaultId()))
            assertIs<DBError>(deleteRes)
            assertEquals(errorNotFound(getDefaultId()), deleteRes)
            // try read - fail
            val readRes = repo.readImage(DBImageId(getDefaultId()))
            assertIs<DBError>(readRes)
            assertEquals(errorNotFound(getDefaultId()), readRes)
        }
    }

    @Test
    fun searchImagesTest() {
        val repo = ImageRepoDB(params = params, randomId = { getDefaultId() })
        val image1 = PsImageStubsItems.FULL_TO_PSIMAGE.copy(title = "CriteriaA")
        val image2 = PsImageStubsItems.FULL_TO_PSIMAGE.copy(title = "CriteriaB")
        initRepo(repo, listOf(image1, image2 ))
        runRepoTest {
            val resTwo = repo.searchImages(DBImageSearchFilter("Criteria"))
            assertIs<DBGetImages>(resTwo)
            assertTrue(resTwo.images.size == 2)
            assertEquals<PsImage>(image1, resTwo.images[0])
            assertEquals<PsImage>(image2, resTwo.images[1])

            val resOne = repo.searchImages(DBImageSearchFilter("CriteriaA"))
            assertIs<DBGetImages>(resOne)
            assertTrue(resOne.images.size == 1)
            assertEquals<PsImage>(image1, resOne.images[0])

            val resNone = repo.searchImages(DBImageSearchFilter("NOT_FOUND"))
            assertIs<DBGetImages>(resNone)
            assertTrue(resOne.images.size == 0)
        }
    }
}
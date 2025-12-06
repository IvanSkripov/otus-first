import kotlinx.coroutines.Dispatchers
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.stubs.getDefaultId
import ru.otus.kotlin.course.common.stubs.repo.ImageRepoInMemory
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertTrue
import ru.otus.kotlin.course.common.models.PsImageId
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.common.stubs.PsImageStubsItems
import kotlin.time.Duration.Companion.minutes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


class ImageRepoInMemoryTest {

    private fun initRepo (repo: ImageRepoInMemory, objects: List<PsImage>)
        = RepoInitializer(repo, objects )

    private fun runRepoTest(testRun: suspend TestScope.() -> Unit) = runTest(timeout = 2.minutes) {
        withContext(Dispatchers.Default) {
            testRun()
        }
    }

    @Test
    fun readImageTest () {
        val repo = ImageRepoInMemory( randomId = { getDefaultId() } )
        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
        runRepoTest {
            val res = repo.readImage(DBImageId(getDefaultId()), false)
            assertIs<DBGetImage>(res)
            val obj = res.image
            assertEquals<PsImage>(PsImageStubsItems.FULL_TO_PSIMAGE, obj)
        }
    }

    @Test
    fun updateImageTest () {
        val repo = ImageRepoInMemory( randomId = { getDefaultId() } )
        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
        runRepoTest {
            val image = PsImageStubsItems.FULL_TO_PSIMAGE.copy(imageUrl = "www.kremlin.ru")
            repo.updateImage(DBImageRequest( image),)
            val res = repo.readImage(DBImageId(getDefaultId()), false)
            assertIs<DBGetImage>(res)
            val obj = res.image
            assertEquals<PsImage>(image, obj)
        }
    }

    @Test
    fun createImageTest () {
        val repo = ImageRepoInMemory( updateLogic = {old, new -> } )
        initRepo(repo, emptyList())
        runRepoTest {
            val cr = repo.createImage(DBImageRequest( PsImageStubsItems.FULL_TO_PSIMAGE))
            assertIs<DBGetImage>(cr)
            val res = repo.readImage(DBImageId(cr.image.id.asString()), false)
            assertIs<DBGetImage>(res)
            val obj = res.image
            val image = PsImageStubsItems.FULL_TO_PSIMAGE.copy(id = cr.image.id )
            assertEquals<PsImage>(image, obj)
        }
    }

    @Test
    fun deleteImageTest () {
        val repo = ImageRepoInMemory( randomId = { getDefaultId() } )
        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
        runRepoTest {
            val res = repo.deleteImage(DBImageId(getDefaultId()))
            assertIs<DBGetImage>(res)
            val obj = res.image
            assertEquals<PsImage>(PsImageStubsItems.FULL_TO_PSIMAGE, obj)
            val notRes = repo.readImage(DBImageId(getDefaultId()), false)
            assertIs<DBError>(notRes)
            assertEquals(errorNotFound(getDefaultId()), notRes)
        }
    }

    @Test
    fun searchImagesTest() {
        val repo = ImageRepoInMemory( randomId = { getDefaultId() } )
        val image1 = PsImageStubsItems.FULL_TO_PSIMAGE.copy(title = "CriteriaA", id = PsImageId("321"))
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
            assertTrue(resNone.images.size == 0)
        }
    }

}
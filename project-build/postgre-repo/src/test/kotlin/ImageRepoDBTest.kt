package ru.otus.kotlin.course.common.stubs.repo.postgre.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import ru.otus.kotlin.course.common.models.PsImage
import ru.otus.kotlin.course.common.repo.*
import ru.otus.kotlin.course.common.stubs.PsImageStubsItems
import ru.otus.kotlin.course.common.stubs.getDefaultId
import ru.otus.kotlin.course.common.stubs.repo.postgre.ImageRepoDB
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes


class ImageRepoDBTest {

    private fun initRepo (repo: ImageRepoDB, objects: List<PsImage>)
        = RepoInitializer(repo, objects )

    private fun runRepoTest(testRun: suspend TestScope.() -> Unit) = runTest(timeout = 2.minutes) {
        withContext(Dispatchers.Default) {
            testRun()
        }
    }

    val postgresDelegate = lazy {
        PostgreSQLContainer(
            DockerImageName.parse("postgres:9.6.12").asCompatibleSubstituteFor("postgres")
        ).also { it.start() }
    }

    val postgres: PostgreSQLContainer by postgresDelegate



    @After
    fun tearDown() {
        postgres.stop()
    }

    @Test
    fun readImageTest () {
        println("postgres.jdbcUrl = ${postgres.jdbcUrl}")
        //val repo = ImageRepoDB( randomId = { getDefaultId() } )
        //initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
//        runRepoTest {
//            val res = repo.readImage(DBImageId(getDefaultId()))
//            assertIs<DBGetImage>(res)
//            val obj = res.image
//            assertEquals<PsImage>(PsImageStubsItems.FULL_TO_PSIMAGE, obj)
//        }
    }

//    @Test
//    fun updateImageTest () {
//        val repo = ImageRepoInMemory( randomId = { getDefaultId() } )
//        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
//        runRepoTest {
//            val image = PsImageStubsItems.FULL_TO_PSIMAGE.copy(imageUrl = "www.kremlin.ru")
//            repo.updateImage(DBImageRequest( image),)
//            val res = repo.readImage(DBImageId(getDefaultId()))
//            assertIs<DBGetImage>(res)
//            val obj = res.image
//            assertEquals<PsImage>(image, obj)
//        }
//    }
//
//    @Test
//    fun createImageTest () {
//        val repo = ImageRepoInMemory( updateLogic = {old, new -> } )
//        initRepo(repo, emptyList())
//        runRepoTest {
//            val cr = repo.createImage(DBImageRequest( PsImageStubsItems.FULL_TO_PSIMAGE))
//            assertIs<DBGetImage>(cr)
//            val res = repo.readImage(DBImageId(cr.image.id.asString()))
//            assertIs<DBGetImage>(res)
//            val obj = res.image
//            val image = PsImageStubsItems.FULL_TO_PSIMAGE.copy(id = cr.image.id )
//            assertEquals<PsImage>(image, obj)
//        }
//    }
//
//    @Test
//    fun deleteImageTest () {
//        val repo = ImageRepoInMemory( randomId = { getDefaultId() } )
//        initRepo(repo, listOf(PsImageStubsItems.FULL_TO_PSIMAGE))
//        runRepoTest {
//            val res = repo.deleteImage(DBImageId(getDefaultId()))
//            assertIs<DBGetImage>(res)
//            val obj = res.image
//            assertEquals<PsImage>(PsImageStubsItems.FULL_TO_PSIMAGE, obj)
//            val notRes = repo.readImage(DBImageId(getDefaultId()))
//            assertIs<DBError>(notRes)
//            assertEquals(errorNotFound(getDefaultId()), notRes)
//        }
//    }

}
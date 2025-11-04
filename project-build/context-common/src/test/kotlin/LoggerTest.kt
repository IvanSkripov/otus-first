import io.klogging.logger
import io.klogging.config.ANSI_CONSOLE
import io.klogging.config.loggingConfiguration
import io.klogging.noCoLogger
import kotlinx.coroutines.delay
import org.junit.Test

class LoggerTest {
    @Test
    fun checkLogger() {
        loggingConfiguration { ANSI_CONSOLE() }
        val logger = noCoLogger("checkLogger")
        logger.info("Test of KLogging")
        Thread.sleep(500)
    }
}
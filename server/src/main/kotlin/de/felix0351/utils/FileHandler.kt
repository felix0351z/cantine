package de.felix0351.utils

import com.charleskorn.kaml.Yaml
import de.felix0351.models.AuthenticationProperties
import de.felix0351.models.ConfigFile
import de.felix0351.models.DatabaseProperties
import de.felix0351.models.DefaultAdmin
import de.felix0351.models.errors.FileIOException
import de.felix0351.models.objects.Collections
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.File

object FileHandler {

    private val logger = getLogger()
    const val FILE_NAME = "config.yaml"
    const val SESSION_FILE_NAME = ".sessions"
    const val CONTENT_FOLDER_NAME = "content"

    lateinit var configuration: ConfigFile

    fun load() {
        File(CONTENT_FOLDER_NAME).mkdir()
        File(SESSION_FILE_NAME).mkdir()
        val config = File(FILE_NAME)

        if(!config.exists()) {
            config.createNewFile()

            logger.info("Creating config.yaml file")
            initConfig(config)

            fail(RuntimeException("Config not configured!"))
        }

        configuration = Yaml.default.decodeFromStream(ConfigFile.serializer(), config.inputStream())
    }


    private fun initConfig(file: File) {
        val example = Yaml.default.encodeToString(
            serializer = ConfigFile.serializer(),
            value = EXAMPLE_CONFIG
        )

        file.writeText(example)
    }

    private val EXAMPLE_CONFIG = ConfigFile(
        port = 8080,
        database = DatabaseProperties(
            host = "localhost",
            port = 27017,
            database = "cantine",
            username = null,
            password = null,
            // Also SCRAM-SHA-1 is supported
            authMechanism = "SCRAM-SHA-256",
            timeout = 2000 // 2 Seconds
        ),
        authentication = AuthenticationProperties(
            session_age = 60, //Days
            sign_key = randomString(28),
            auth_key = randomString(32),
            pepper = randomString(16, complexCharset = true),
            startUser = DefaultAdmin(
                username = "admin",
                name = "Admin",
                password = "admin",
                credit = 100F
            )
        )
    )

    private suspend fun saveContentFile(file: PartData.FileItem, path: String) = withContext(Dispatchers.IO) {
        val bytes = file.streamProvider().readBytes()
        File("$CONTENT_FOLDER_NAME/$path").writeBytes(bytes)
    }

    fun createPathForContentFile(id: String, collection: Collections): String {
        File("$CONTENT_FOLDER_NAME/${collection.name}").mkdir()
        // Collection name + object id
        return "${collection.name}/$id"
    }

    fun getContentFile(dir: String, id: String): File = File("$CONTENT_FOLDER_NAME/$dir/$id")

    fun deleteContentFile(path: String) = File("$CONTENT_FOLDER_NAME/$path").delete()

    /**
     * Saves the file
     **/
    suspend fun savePicture(file: PartData.FileItem, path: String) {
        try {
            saveContentFile(file, path)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FileIOException()
        }
    }

    /**
     * Deletes the old content of the file and saves the new file.
     * If an existing file can't be found, the new file will be added anyway.
     */
    suspend fun updatePicture(file: PartData.FileItem, path: String) {
        try {
            deleteContentFile(path)
            saveContentFile(file, path)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FileIOException()
        }
    }
}
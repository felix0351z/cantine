package de.felix0351.utils

import com.charleskorn.kaml.Yaml
import de.felix0351.models.AuthenticationProperties
import de.felix0351.models.ConfigFile
import de.felix0351.models.DatabaseProperties
import org.slf4j.LoggerFactory
import java.io.File


object FileHandler {

    private val logger = LoggerFactory.getLogger(FileHandler.javaClass)
    private const val FILE_NAME = "config.yaml"

    lateinit var configuration: ConfigFile

    fun load() {
        val config = File(FILE_NAME)

        if(!config.exists()) {
            config.createNewFile()

            logger.info("Creating config.yaml file")
            initConfig(config)
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
            type = "sqlite",
            url = "data.db",
            username = null,
            password = null
        ),
        authentication = AuthenticationProperties(
            session_age = 60, //Days
            sign_key = randomString(28),
            auth_key = randomString(32),
            pepper = randomString(16, complexCharset = true),
        )
    )

}
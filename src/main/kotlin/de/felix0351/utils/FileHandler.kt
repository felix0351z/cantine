package de.felix0351.utils

import com.charleskorn.kaml.Yaml
import de.felix0351.fail
import de.felix0351.models.ConfigFile
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

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
        // Load example from recourse folder
        val example = this.javaClass.classLoader.getResource("config.yaml")
        if (example == null) {
            //Without a correct configuration file, the server can't start
            fail(IOException("Failed to load example config.yaml"))
        }

        else example.openStream().copyTo(file.outputStream())
    }



}
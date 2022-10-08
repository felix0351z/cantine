package de.felix0351.utils

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
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
        if (example == null) throw IOException("Failed to load example config.yaml")

        else example.openStream().copyTo(file.outputStream())
    }



    /**
     * Config file for the server, which will be read as yaml
     *
     * @property port Port, where the server will run on
     * @property databaseType The name, which database should be used (mysql, mariadb, or sqlite)
     * @property databaseProperties Properties for the connection to the database
     *
     */
    @Serializable
    data class ConfigFile(
        val port: Int,
        val databaseType: String,
        val databaseProperties: DatabaseProperties
    )


    /**
     * Properties for the database connection
     *
     * @property url Adress to the database
     * @property port Standard sql port is 1433
     * @property username Username for the database
     * @property password Password for the database
     */
    @Serializable
    data class DatabaseProperties(
        val url: String,
        val port: Int?,
        val username: String?,
        val password: String?
    )

    enum class DatabaseDriver(val driver: String) {
        MYSQL("com.mysql.cj.jdbc.Driver"),
        MARIADB("com.mysql.cj.jdbc.Driver"),
        SQLITE("org.sqlite.JDBC")
    }


}
package de.felix0351.db

import de.felix0351.errors.InternalDatabaseException
import de.felix0351.models.DatabaseProperties
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Collections
import de.felix0351.models.objects.Content
import de.felix0351.utils.FileHandler
import de.felix0351.utils.fail
import de.felix0351.utils.getLogger

import com.mongodb.*
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import kotlin.jvm.Throws


/**
 * Creates a new mongodb connection via the configuration from the config
 *
 *  https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/connection/connect/
 */
class MongoDBConnection {

    private val client: CoroutineClient
    private val log = getLogger()

    val database: CoroutineDatabase

    init {
        try {
            val config = FileHandler.configuration.database

            client = KMongo.createClient(buildSettingsObject(config)).coroutine

            database = client.getDatabase(config.database)
            //Check for a successful connection
            runBlocking {
                database.runCommand<Unit>("{ping: 1}", ReadPreference.nearest())
            }

        } catch (ex: MongoException) {
            // If something goes wrong while connecting to the database the server can't start

            val internalError = handlePossibleExceptions(ex)
            fail(internalError)
        }
    }


    /**
     *  Close the connection to the source
     */
    fun close() {
        client.close()
    }


    /**
     * Handles all necessary errors.
     * For the errors which can't be hold, a fail() will be called
     *
     * @see fail
     */
    fun handlePossibleExceptions(ex: MongoException): RuntimeException {

        when (ex) {

            // If something is wrong with the executed command (syntax, authorization etc.) the server can't work any longer
            is MongoSecurityException -> {

                if (ex.cause is MongoCommandException) {
                    val cmdex = ex.cause as MongoCommandException
                    // Authentication error
                    if (cmdex.code == 18) log.error("Failed to authenticate to the server!")
                    // Unauthorized call
                    if(cmdex.code == 13) log.error("Unauthorized call!")
                }


                fail(ex)
            }

            // If the server isn't compatible there is no way to use the server
            is MongoIncompatibleDriverException -> {
                log.error("The driver version is not compatible with the named server")
                fail(ex)
            }


            // If timed out, the server can't any longer
            is MongoTimeoutException, is MongoExecutionTimeoutException -> {
                log.error("Timed out while connecting to ${FileHandler.configuration.database.host}")
                fail(ex)
            }

            // Internal server exception, no need to fail
            is MongoServerException -> {
                log.error("Exception on mongodb server occured!", ex)

                return InternalDatabaseException()
            }

            else -> {
                fail(ex)
            }

        }
    }

    /**
     * Inline function to wrap db calls around an error handling system
     *
     *  If the return type is null, an error occurred, and the call wasn't executed correctly
     *
     *  Fatal errors could also cause to a crash of the complete server
     * @throws InternalDatabaseException
     */

    @Throws(InternalDatabaseException::class)
    inline fun<T> call(fn: (db: CoroutineDatabase) -> T): T {
        try {
            return fn(database)
        } catch (ex: MongoException) {
            throw handlePossibleExceptions(ex)
        }
    }

    /**
     *  Wrapper around the normal call function
     *
     *  Get the User Collection directly instead of getting a db value with the normal call function
     *  @see call
     *  @throws InternalDatabaseException
     */
    @Throws(InternalDatabaseException::class)
    inline fun<T> callToUserCollection(fn: (col: CoroutineCollection<Auth.User>) -> T): T {
        return call {
            val col = it.getCollection<Auth.User>(Collections.USERS.name)
            fn(col)
        }
    }

    /**
     *  Wrapper around the normal call function
     *
     *  Get the Meal Collection directly instead of getting a db value with the normal call function
     *  @see call
     *  @throws InternalDatabaseException
     */
    @Throws(InternalDatabaseException::class)
    inline fun<T> callToMealsCollection(fn: (col: CoroutineCollection<Content.Meal>) -> T): T {
        return call {
            val col = it.getCollection<Content.Meal>(Collections.MEALS.name)
            fn(col)
        }
    }


    companion object {

        /**
         *  Build a mongodb settings object
         *  https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/connection/mongoclientsettings/
          */
        private fun buildSettingsObject(config: DatabaseProperties): MongoClientSettings {
            val str = buildConnectionString(config)

            // Set the timeout
            val settings = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(str))
                .applyToClusterSettings { builder ->
                    if (config.timeout != null) {
                        builder.serverSelectionTimeout(config.timeout, TimeUnit.MILLISECONDS)
                    }
                }
                .build()

            return settings
        }


        /**
         * Standard connection String format for MongoDB
         * mongodb://[username:password@]host1[:port1]/[defaultauthdb][?options]]
         *
         * https://www.mongodb.com/docs/manual/reference/connection-string/
         *
         */
        private fun buildConnectionString(config: DatabaseProperties): String {

            // If username or password are not mentioned return without it.
            if ((config.username == null) || (config.password == null)) {
                return "mongodb://${config.host}:${config.port}/${config.database}"
            }

            val str =  "mongodb://${config.username}:${config.password}@" + // Username:Password@
                    "${config.host}:${config.port}/" + // Host:Port/
                   "?authMechanism=${config.authMechanism}" //Auth-mechanism

            getLogger().debug("Connection string: $str")
            return str
        }


    }




}
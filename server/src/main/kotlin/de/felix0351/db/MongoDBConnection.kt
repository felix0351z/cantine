package de.felix0351.db


import de.felix0351.models.DatabaseProperties
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Collections
import de.felix0351.utils.FileHandler
import de.felix0351.utils.fail

import com.mongodb.MongoException

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


/**
 *
 *  https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/connection/connect/
 *
 *  TODO: Handle mongodb exceptions for timeout, etc.
 *
 */
class MongoDBConnection {

    private val client: CoroutineClient
    val database: CoroutineDatabase

    init {
        try {
            val config = FileHandler.configuration.database

            client = KMongo.createClient(buildConnectionString(config)).coroutine
            database = client.getDatabase(config.database)
        } catch (e: Exception) {
            // If something goes wrong while connecting to the database the server can't start
            fail(e)
        }
    }


    /**
     *  Close the connection to the source
     */
    fun close() {
        client.close()
    }

    /**
     * Inline function to wrap db calls around an error handling system
     *
     *  If the return type is null, an error occurred, and the call wasn't executed correctly
     *
     *  Fatal errors could also cause to a crash of the complete server
     *
     */
    inline fun<T> call(fn: (db: CoroutineDatabase) -> T): T? {
        try {
            return fn(database)
        } catch (ex: MongoException) {

            ex.code


            TODO()
        }

        return null
    }

    /**
     *  Wrapper around the normal call function
     *
     *  Get the User Collection directly instead of getting a db value with the normal call function
     *  @see call
     */
    inline fun<T> callToUserCollection(fn: (col: CoroutineCollection<Auth.User>) -> T): T? {
        return call {
            val col = it.getCollection<Auth.User>(Collections.USERS.name)
            fn(col)
        }
    }


    companion object {


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
                return "mongodb://${config.host}:${config.port}/${config.database}?serverSelectionTimeoutMS=2000"
            }

            return "mongodb://${config.username}:${config.password}@${config.host}:${config.port}/${config.database}?serverSelectionTimeoutMS=2000"

        }


    }




}
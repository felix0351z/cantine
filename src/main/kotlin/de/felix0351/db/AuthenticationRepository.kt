package de.felix0351.db

import de.felix0351.models.tables.UserSessions
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class AuthenticationRepository {






    // Only for the Ktor sessions plugin.
    class AuthenticationSessionStorage : SessionStorage {

        override suspend fun invalidate(id: String) {
            transaction {
                UserSessions.deleteWhere { UserSessions.id eq id }
            }
        }


        override suspend fun read(id: String): String {
            val session = transaction {
                UserSessions.select { UserSessions.id eq id }.first()
            }

            return session[UserSessions.value]
        }

        override suspend fun write(id: String, value: String) {
            transaction {
                UserSessions.insertIgnore {
                    it[UserSessions.id] = id
                    it[UserSessions.value] = value
                }

            }
        }

    }

}
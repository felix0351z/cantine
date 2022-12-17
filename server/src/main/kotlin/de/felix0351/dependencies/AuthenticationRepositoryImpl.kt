package de.felix0351.dependencies

import de.felix0351.models.objects.User
import de.felix0351.models.tables.UserSessions
import de.felix0351.models.tables.Users
import de.felix0351.utils.getLogger
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AuthenticationRepositoryImpl : AuthenticationRepository {

    val logger = getLogger()

    override suspend fun getUserByUsername(username: String): User? {
        val result = Users.select { Users.username eq username }.firstOrNull() ?: return null

        return resultToUser(result)
    }

    override suspend fun getUsers(): List<User> {
        return Users.selectAll().map(::resultToUser)
    }

    override suspend fun addUser(user: User) {
        Users.insert {
            it[username] = user.username
            it[name] = user.name
            it[permissionLevel] = user.permissionLevel
            it[hash] = user.hash
        }
    }

    override suspend fun removeUser(username: String) {
        Users.deleteWhere { Users.username eq username }
    }

    private fun resultToUser(result: ResultRow) = User(
        username = result[Users.username],
        name = result[Users.name],
        permissionLevel = result[Users.permissionLevel],
        hash = result[Users.hash]
    )


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
package de.felix0351.dependencies

import de.felix0351.db.MongoDBConnection
import de.felix0351.models.errors.Response
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Auth.User
import de.felix0351.models.errors.ErrorCode
import de.felix0351.utils.getLogger

import org.litote.kmongo.eq
import org.litote.kmongo.setValue


class AuthenticationRepositoryMongoDB(private val con: MongoDBConnection) : AuthenticationRepository {

    val logger = getLogger()

    override suspend fun getUserByUsername(username: String): User? = con.callToUserCollection {
        it.findOne(User::username eq username)
    }

    override suspend fun getUsers(): List<User> = con.callToUserCollection {
        it.find().toList()
    }

    override suspend fun addUser(user: User): Response = con.callToUserCollection {
        if (getUserByUsername(user.username) != null) return@callToUserCollection Response.Error(ErrorCode.AlreadyExists)
        it.insertOne(user)

        Response.Ok
    }

    override suspend fun removeUser(username: String): Response = con.callToUserCollection {
        val count = it.deleteOne( User::username eq username ).deletedCount
        if (count == 0L) return@callToUserCollection Response.Error(ErrorCode.NotFound)

        Response.Ok
    }

    override suspend fun updatePermissionLevel(username: String, level: Auth.PermissionLevel): Response = con.callToUserCollection {
        val res = it.updateOne(User::username eq username, setValue(User::permissionLevel, level))
        if (res.matchedCount == 0L) return@callToUserCollection Response.Error(ErrorCode.NotFound)
        if (res.modifiedCount == 0L) return@callToUserCollection Response.Error(ErrorCode.SameValue)

        Response.Ok
    }

    override suspend fun updateUserHash(username: String, hash: String): Response = con.callToUserCollection {
        val res = it.updateOne( User::username eq username, setValue(User::hash, hash))
        if (res.matchedCount == 0L) return@callToUserCollection Response.Error(ErrorCode.NotFound)
        if (res.modifiedCount == 0L) return@callToUserCollection Response.Error(ErrorCode.SameValue)

        Response.Ok
    }

    override suspend fun updateUserCredit(username: String, hash: String): Response = con.callToUserCollection {
        val matched = it.updateOne(User::username eq username, setValue(User::credit, hash)).matchedCount
        if (matched == 0L) return@callToUserCollection Response.Error(ErrorCode.NotFound)

        Response.Ok
    }

}
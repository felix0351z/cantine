package de.felix0351.repository

import de.felix0351.db.MongoDBConnection
import de.felix0351.models.errors.DatabaseException.*
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Auth.User
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

    override suspend fun getUserCount(): Long = con.callToUserCollection {
        it.countDocuments()
    }

    override suspend fun addUser(user: User): Unit = con.callToUserCollection {
        if (getUserByUsername(user.username) != null) throw ValueAlreadyExistsException()
        it.insertOne(user)

    }

    override suspend fun removeUser(username: String): Unit = con.callToUserCollection {
        // Delete the user. If nothing was deleted, the user wasn't found.
        val count = it.deleteOne( User::username eq username ).deletedCount
        // The deleteCount can't be higher than 1 because the username checking in the addUser() method
        if (count == 0L) throw NotFoundException()
    }

    override suspend fun updatePermissionLevel(username: String, level: Auth.PermissionLevel): Unit = con.callToUserCollection {
        // Update the permission and notify the user if it has already the same permission or if the user wasn't found
        val res = it.updateOne(User::username eq username, setValue(User::permissionLevel, level))
        if (res.matchedCount == 0L) throw NotFoundException()
        if (res.modifiedCount == 0L) throw SameValueException()

    }

    override suspend fun updateUserHash(username: String, hash: String): Unit = con.callToUserCollection {
        // Update the password hash of the user. Notify if the user tries to set the new to the old password or if the username wasn't found
        val res = it.updateOne( User::username eq username, setValue(User::hash, hash))
        if (res.matchedCount == 0L) throw NotFoundException()
    }

    override suspend fun updateUserName(username: String, name: String): Unit = con.callToUserCollection {

        val res = it.updateOne(User::username eq username, setValue(User::name, name))
        if (res.matchedCount == 0L) throw NotFoundException()

    }

    override suspend fun updateUserCredit(username: String, hash: String): Unit = con.callToUserCollection {
        // Update the credit of the user. Because the server handles the charge, the value can't be equal
        val matched = it.updateOne(User::username eq username, setValue(User::credit, hash)).matchedCount
        if (matched == 0L) throw NotFoundException()

    }




}
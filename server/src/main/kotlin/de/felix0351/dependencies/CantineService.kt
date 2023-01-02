package de.felix0351.dependencies

import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.errors.NoPasswordException
import de.felix0351.models.errors.WrongPasswordException
import de.felix0351.models.objects.Auth
import de.felix0351.utils.Hashing
import java.time.Instant

import kotlin.jvm.Throws

//TODO: Hashing-Klasse für das korrekte Hashen von Password und Credit
class CantineService(
     private val authRepo: AuthenticationRepository,
     val contentRepo: ContentRepository
    ) {

    private suspend fun getPrivateUser(session: Auth.UserSession): Auth.User =
        authRepo.getUserByUsername(session.username) ?: throw DatabaseException.NotFoundException()


    /**
     *  Checks the given password of the user if it's correct
     *
     * @param username The user which has the password
     * @param password Password
     *
     * @return Returns true if the password is correct or false if its wrong or the user can't be found
     */
    suspend fun checkUserCredentials(username: String, password: String): Auth.User? {

        val user = authRepo.getUserByUsername(username) ?: return null
        val correct =  Hashing.checkPassword(
            pw = password,
            hash = user.hash
        )

        return if (correct) user else null
    }

    /**
     * Get all registered users
     */
    suspend fun getUsers(): List<Auth.PublicUser> {
        val list = mutableListOf<Auth.PublicUser>()

       authRepo.getUsers().forEach {
           list.add(userToPublicUser(it))
       }

        return list
    }

    /**
     *  Get a user by his username
     *
     * @param username The username lol
     */
    @Throws(DatabaseException.NotFoundException::class)
    suspend fun getUser(username: String): Auth.PublicUser {
        val user = authRepo.getUserByUsername(username) ?: throw DatabaseException.NotFoundException()

        return userToPublicUser(user)
    }

    /**
     *  Change the permission level of the requested user
     *
     * @param session The session if the admin which called the route
     * @param request Request to change the permission level
     *
     * @see Auth.PermissionChangeRequest
     */
    @Throws(NoPasswordException::class, DatabaseException.ValueAlreadyExistsException::class)
    suspend fun addUser(session: Auth.UserSession, request: Auth.UserAddRequest) {
        // If the own password is wrong
        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()

        if (request.user.password == null) throw NoPasswordException()


        authRepo.addUser(Auth.User(
            username = request.user.username,
            name = request.user.name,
            permissionLevel = request.user.permissionLevel,
            credit = "Hashing",
            hash = Hashing.toHash(request.user.password)
        ))
    }

    /**
     *  Delete a user
     *
     * @param session The session of the admin
     * @param request Request to delete the user
     *
     * @see Auth.UserDeleteRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun deleteUser(session: Auth.UserSession, request: Auth.UserDeleteRequest) {
        // If the own password is wrong
        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()

        authRepo.removeUser(request.username)
    }

    /**
     * Change the password of the own account
     *
     * @param session User's session
     * @param request Request to change the password
     *
     * @see Auth.PasswordChangeRequest
     *
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun changeOwnPassword(session: Auth.UserSession, request: Auth.PasswordChangeRequest) {

        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()

        // Set new password
        authRepo.updateUserHash(
            session.username,
            Hashing.toHash(request.newPassword)
        )

    }

    /**
     *  Change the full name of the requested user
     *
     * @param session The session if the admin which called the route
     * @param request Request to change the full name
     *
     * @see Auth.NameChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun changeName(session: Auth.UserSession, request: Auth.NameChangeRequest) {

        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()

        // Set new name for the user
        authRepo.updateUserName(request.username, request.newName)
    }

    /**
     *  Change the password  of the requested user
     *
     * @param session The session if the admin which called the route
     * @param request Request to change the password
     *
     * @see Auth.PasswordChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class, DatabaseException.SameValueException::class)
    suspend fun changePassword(session: Auth.UserSession, request: Auth.PasswordChangeRequest) {
        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()
        //if no username was provided
        if (request.username == null) throw DatabaseException.NotFoundException()

        authRepo.updateUserHash(request.username, Hashing.toHash(request.newPassword))
    }

    /**
     *  Change the permission level of the requested user
     *
     * @param session The session if the admin which called the route
     * @param request Request to change the permission level
     *
     * @see Auth.PermissionChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class, DatabaseException.SameValueException::class)
    suspend fun changePermissionLevel(session: Auth.UserSession, request: Auth.PermissionChangeRequest) {
        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()

        authRepo.updatePermissionLevel(request.username,  request.newPermissionLevel)
    }


    /**
     * Utility function to convert a UserObject to a serializable PublicUser Object
     * Note that the password will automatically set to null, for security purpose
     *
     * @see Auth.PublicUser
     */
    fun userToPublicUser(user: Auth.User) = Auth.PublicUser(
        username = user.username,
        name = user.name,
        permissionLevel = user.permissionLevel,
        credit = Hashing.getCredit(user.hash)
    )

}
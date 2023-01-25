package de.felix0351.services

import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.errors.NoPasswordException
import de.felix0351.models.errors.WrongPasswordException
import de.felix0351.models.objects.*
import de.felix0351.repository.AuthenticationRepository
import de.felix0351.utils.Hashing
import kotlin.jvm.Throws

class AuthenticationService(
    private val authRepo: AuthenticationRepository
) {


    suspend fun getPrivateUser(session: Auth.UserSession): Auth.User =
        authRepo.getUserByUsername(session.username) ?: throw DatabaseException.NotFoundException()


    /**
     *  Checks the given password of the user if it's correct
     *
     * @param username The user which has the password
     * @param password Password
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
     * @param self The Admin which called the route
     * @param request Request to change the permission level
     * @see PermissionChangeRequest
     */
    @Throws(NoPasswordException::class, DatabaseException.ValueAlreadyExistsException::class)
    suspend fun addUser(self: Auth.User, request: UserAddRequest) {
        // If the own password is wrong
        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()

        if (request.user.password == null) throw NoPasswordException()


        authRepo.addUser(
            Auth.User(
            username = request.user.username,
            name = request.user.name,
            permissionLevel = request.user.permissionLevel,
            credit = Hashing.encryptCredit(request.user.credit),
            hash = Hashing.toHash(request.user.password)
        ))
    }

    /**
     *  Delete a user
     *
     * @param self The admin which called the route
     * @param request Request to delete the user
     * @see UserDeleteRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun deleteUser(self: Auth.User, request: UserDeleteRequest) {
        // If the own password is wrong
        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()

        authRepo.removeUser(request.username)
    }

    /**
     * Change the password of the own account
     *
     * @param self The user which called the route
     * @param request Request to change the password
     * @see PasswordChangeRequest
     *
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun changeOwnPassword(self: Auth.User, request: PasswordChangeRequest) {

        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()

        // Set new password
        authRepo.updateUserHash(
            self.username,
            Hashing.toHash(request.newPassword)
        )

    }

    /**
     *  Change the full name of the requested user
     *
     * @param self The admin which called the route
     * @param request Request to change the full name
     * @see NameChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun changeName(self: Auth.User, request: NameChangeRequest) {

        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()

        // Set new name for the user
        authRepo.updateUserName(
            request.username,
            request.newName
        )
    }

    /**
     *  Change the password  of the requested user
     *
     * @param self The admin which called the route
     * @param request Request to change the password
     * @see PasswordChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class, DatabaseException.SameValueException::class)
    suspend fun changePassword(self: Auth.User, request: PasswordChangeRequest) {
        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()
        //if no username was provided
        if (request.username == null) throw DatabaseException.NotFoundException()

        authRepo.updateUserHash(
            request.username,
            Hashing.toHash(request.newPassword)
        )
    }

    /**
     *  Change the permission level of the requested user
     *
     * @param self The admin which called the route
     * @param request Request to change the permission level
     * @see PermissionChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class, DatabaseException.SameValueException::class)
    suspend fun changePermissionLevel(self: Auth.User, request: PermissionChangeRequest) {
        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()

        authRepo.updatePermissionLevel(request.username,  request.newPermissionLevel)
    }

    /**
     * Utility function to convert a UserObject to a serializable PublicUser Object
     * Note that the password will automatically set to null, for security purpose
     * @see Auth.PublicUser
     */
    private fun userToPublicUser(user: Auth.User) = Auth.PublicUser(
        username = user.username,
        name = user.name,
        permissionLevel = user.permissionLevel,
        credit = Hashing.decryptCredit(user.credit)
    )

}
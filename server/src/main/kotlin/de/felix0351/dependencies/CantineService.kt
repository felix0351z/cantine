package de.felix0351.dependencies

import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.errors.IncorrectOrderException
import de.felix0351.models.errors.NoPasswordException
import de.felix0351.models.errors.WrongPasswordException
import de.felix0351.models.objects.*
import de.felix0351.utils.Hashing
import org.litote.kmongo.newId
import java.time.Instant

import kotlin.jvm.Throws

//TODO: Hashing-Klasse für das korrekte Hashen von Password und Credit
class CantineService(
     private val authRepo: AuthenticationRepository,
     private val paymentRepo: PaymentRepository,
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
     * @see PermissionChangeRequest
     */
    @Throws(NoPasswordException::class, DatabaseException.ValueAlreadyExistsException::class)
    suspend fun addUser(session: Auth.UserSession, request: UserAddRequest) {
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
     * @see UserDeleteRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun deleteUser(session: Auth.UserSession, request: UserDeleteRequest) {
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
     * @see PasswordChangeRequest
     *
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun changeOwnPassword(session: Auth.UserSession, request: PasswordChangeRequest) {

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
     * @see NameChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun changeName(session: Auth.UserSession, request: NameChangeRequest) {

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
     * @see PasswordChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class, DatabaseException.SameValueException::class)
    suspend fun changePassword(session: Auth.UserSession, request: PasswordChangeRequest) {
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
     * @see PermissionChangeRequest
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class, DatabaseException.SameValueException::class)
    suspend fun changePermissionLevel(session: Auth.UserSession, request: PermissionChangeRequest) {
        //if the password of admin is wrong
        if (!Hashing.checkPassword(request.password, getPrivateUser(session).hash)) throw WrongPasswordException()

        authRepo.updatePermissionLevel(request.username,  request.newPermissionLevel)
    }


    /**
     * Create an order from a CreateOrderRequest
     *
     * Add the order to database and update the credit amount of the user
     *
     * @param username User which initiated the request
     * @param request The create request
     *
     * @see CreateOrderRequest
     *
     */
    @Throws(IncorrectOrderException::class, DatabaseException.NotFoundException::class)
    suspend fun createOrder(username: String, request: CreateOrderRequest) {
        // Serialize order (check for meals if their exists) -> Update credit -> Add Order to db

        // All selected meals by the user as id
        val ids = request.meals.map { it.meal }
        val pair = try {
            paymentRepo.getMealsAndCredit(username, ids)
        } catch (ex: DatabaseException.NotFoundException) {
            throw IncorrectOrderException()
        }

        // Create a map with the meals from database and the id as key
        val meals = pair.first.associateBy { it.id }

        var fullPrice = 0F
        var fullDeposit = 0F
        val orderMeals = request.meals.map {
            // Get the meal by the key id value and add it to the order
            val meal = meals[it.meal]!!

            // Add the price and deposit to the full values of the order
            fullPrice += meal.price
            fullDeposit += meal.deposit

            // Build the ordered meal
            mealToOrderedMeal(meal, it.selections)
        }

        // Multiply with -1, so that it will be removed from the credit
        val toPay = (fullDeposit + fullPrice) * (-1)

        //TODO: Hashing toNewCredit(pair.b)
        //Check if the amount is to low

        paymentRepo.addOrder(
            order = Content.Order(
                id = newId(),
                user = username,
                meals = orderMeals,
                price = fullPrice,
                deposit = fullDeposit,
                orderTime = Instant.now()
            ),
            username = username,
            credit = "")
    }

    suspend fun cancelOrder(username: String, request: DeleteOrderRequest) {
        // Update credit ->  Cancel order
        val pair = paymentRepo.getOrderAndCredit(username, request.order)
        //TODO Hashing
        val newCredit = ""

        paymentRepo.cancelOrder(
            username = username,
            id = request.order,
            credit = newCredit
        )
    }

    suspend fun verifyOrder() {
        TODO()
    }

    private fun mealToOrderedMeal(meal: Content.Meal, selections: List<String>) = Content.OrderedMeal(
        name = meal.name,
        description = meal.description,
        price = meal.price,
        deposit = meal.deposit,
        day = meal.day,
        selections = selections,
        picture = meal.picture
    )

    /**
     * Utility function to convert a UserObject to a serializable PublicUser Object
     * Note that the password will automatically set to null, for security purpose
     *
     * @see Auth.PublicUser
     */
    private fun userToPublicUser(user: Auth.User) = Auth.PublicUser(
        username = user.username,
        name = user.name,
        permissionLevel = user.permissionLevel,
        credit = Hashing.getCredit(user.hash)
    )

    suspend fun getOrdersFromUser(username: String) = paymentRepo.getOrdersFromUser(username)

    suspend fun getPayments(username: String): List<Auth.Payment> = paymentRepo.getPayments(username, Instant.ofEpochSecond(0))

    suspend fun clearPayments(username: String) = paymentRepo.clearPayments(username)
}
package de.felix0351.services

import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.errors.IncorrectOrderException
import de.felix0351.models.errors.NotEnoughMoneyException
import de.felix0351.models.errors.WrongPasswordException
import de.felix0351.models.objects.*
import de.felix0351.repository.AuthenticationRepository
import de.felix0351.repository.PaymentRepository
import de.felix0351.utils.Hashing
import org.litote.kmongo.Id

import org.litote.kmongo.newId
import java.time.Instant
import kotlin.jvm.Throws

class PaymentService(
    private val paymentRepo: PaymentRepository,
    private val authRepo: AuthenticationRepository
) {

    /* Order Functions */

    /**
     * Create an order from a CreateOrderRequest.
     * Add the order to database and update the credit amount of the user
     *
     * @param self The user which called the route
     * @param request The create request
     * @see CreateOrderRequest
     *
     */
    @Throws(IncorrectOrderException::class, DatabaseException.NotFoundException::class)
    suspend fun createOrder(self: Auth.User, request: CreateOrderRequest): Content.Order {
        // Serialize order (check for meals if their exists) -> Update credit -> Add Order to db

        // All selected meals by the user as id
        val ids = request.meals.map { it.id }
        val mealsInDB = try {
            paymentRepo.getMeals(ids)
        } catch (ex: DatabaseException.NotFoundException) {
            throw IncorrectOrderException()
        }

        // Create a map with the meals from database and the id as key
        val mealsMap = mealsInDB.associateBy { it.id }

        var fullPrice = 0F
        var fullDeposit = 0F
        val orderMeals = request.meals.map {
            // Get the meal by the key id value and add it to the order
            val meal = mealsMap[it.id]!!

            // Add the price and deposit to the full values of the order
            fullPrice += meal.price
            fullDeposit += meal.deposit

            // Build the ordered meal
            mealToOrderedMeal(meal, it.selections)
        }


        // Update credit
        var currentCredit = Hashing.decryptCredit(self.credit)
        currentCredit -= (fullPrice + fullDeposit)

        if (currentCredit < 0.0) { //Not enough money!
            throw NotEnoughMoneyException(currentCredit)
        }

        val finalOrder = Content.Order(
            id = newId(),
            user = self.username,
            meals = orderMeals,
            price = fullPrice,
            deposit = fullDeposit,
            orderTime = Instant.now()
        )

        paymentRepo.addOrder(
            order = finalOrder,
            username = self.username,
            credit = Hashing.encryptCredit(currentCredit))

        return finalOrder
    }

    /**
     * Verify an order.
     * will be deleted and inserted as payment
     * @see VerifyOrderRequest
     */
    @Throws(DatabaseException.NotFoundException::class)
    suspend fun verifyOrder(request: VerifyOrderRequest) {
        val order = paymentRepo.getOrder(request.orderId)
        val payment = Auth.Payment(
            user = request.username,
            meals = order.meals.map { it.name }, //Map the names of the ordered meals for the payment info
            price = order.price,
            creationTime = Instant.now()
        )

        paymentRepo.verifyAndDeleteOrder(request.orderId, payment)
    }

    /**
     * Cancel a current order.
     * Remove the order and give the user back his credit

     * @param self The user which called the route
     * @param id ID of the order
     */
    @Throws(DatabaseException.NotFoundException::class)
    suspend fun cancelOrder(self: Auth.User, id: Id<Content.Order>): Content.Order {
        // Update credit ->  Cancel order
        val order = paymentRepo.getOrder(id)

        var currentCredit = Hashing.decryptCredit(self.credit)
        currentCredit += order.price

        paymentRepo.cancelOrder(
            username = self.username,
            id = id,
            credit = Hashing.encryptCredit(currentCredit)
        )

        return order
    }

    /**
     * Get all orders from a user
     * @param username The username of the user
     */
    suspend fun getOrdersFromUser(username: String) = paymentRepo.getOrdersFromUser(username)

    /* Credit Functions */

    /**
     * Get the current credit of a user
     * @param user The user
     */
    fun getCreditFromUser(user: Auth.User): Float {
        return Hashing.decryptCredit(user.credit)
    }

    /**
     * Increase the credit of the user
     *  @param self The user which called the route
     *  @param request The Request
     */
    @Throws(WrongPasswordException::class, DatabaseException.NotFoundException::class)
    suspend fun addCreditToUser(self: Auth.User, request: AddCreditRequest) {
        // If the own password is wrong
        if (!Hashing.checkPassword(request.password, self.hash)) throw WrongPasswordException()

        var currentCredit = Hashing.decryptCredit(self.credit)
        currentCredit += request.credit

        authRepo.updateUserCredit(
            username = request.username,
            hash = Hashing.encryptCredit(currentCredit)
        )
    }

    /* Payment Functions */

    /**
     * Get all payments from a user
     * @param username The username of the user
     */
    suspend fun getPayments(username: String): List<Auth.Payment> = paymentRepo.getPayments(username, Instant.ofEpochSecond(0))

    /**
     * Delete all payments from a user
     * @param username The username of the user
     */
    suspend fun clearPayments(username: String) = paymentRepo.clearPayments(username)


    /**
     * Utility function to convert a Meal to a OrderedMeal Object
     * @see Content.OrderedMeal
     */
    private fun mealToOrderedMeal(meal: Content.Meal, selections: List<String>) = Content.OrderedMeal(
        id = meal.id.toString(),
        name = meal.name,
        description = meal.description,
        price = meal.price,
        deposit = meal.deposit,
        day = meal.day,
        selections = selections,
        picture = meal.picture
    )
}



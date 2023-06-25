package de.juliando.app.android.ui.home.shopping_cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.ui.OrderedMeal
import de.juliando.app.repository.PaymentRepository
import de.juliando.app.utils.asDisplayable
import de.juliando.app.utils.toCurrencyString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingCartViewModel(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    data class ShoppingCartItems(
        val items: List<OrderedMeal>,
        val amount: String
    )

    // Hold the necessary information for the view in one state
    private val _meals = MutableStateFlow<DataState>(DataState.Loading)
    val meals = _meals.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                // Format the raw ordered meals data to displayable elements
                val meals = paymentRepository.shoppingCart
                val amount = meals.sumOf { (it.price + it.deposit).toDouble() }.toFloat()

                _meals.value = DataState.Success(ShoppingCartItems(meals.asDisplayable(), toCurrencyString(amount)))
            } catch (ex: Exception) {
                Log.e(this@ShoppingCartViewModel::class.toString(), "Error occurred while loading the shopping cart", ex)
                _meals.value = DataState.Error(ex)
            }
        }
    }

    fun onDeleteClick(id: String) {
        paymentRepository.removeItemFromShoppingCart(id)
        _meals.value = DataState.Success(paymentRepository.shoppingCart)
    }

    fun onPaymentClick() {
        TODO()
    }




}
package de.juliando.app.android.ui.home.shopping_cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.backend.CreateOrderRequest
import de.juliando.app.models.objects.backend.CreateOrderRequestMeal
import de.juliando.app.models.objects.ui.OrderedMeal
import de.juliando.app.repository.PaymentRepository
import de.juliando.app.utils.asDisplayable
import de.juliando.app.utils.toCurrencyString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private suspend fun receiveNewData(paymentRepository: PaymentRepository): ShoppingCartViewModel.ShoppingCartItems {
    // Format the raw ordered meals data to displayable elements
    val meals = paymentRepository.shoppingCart
    // Sum up the prices
    val amount = meals.sumOf { (it.price + it.deposit).toDouble() }.toFloat()

    return ShoppingCartViewModel.ShoppingCartItems(
        meals.asDisplayable(), toCurrencyString(amount)
    )
}


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

    // Every time the bottom sheet will be loaded, the current shopping cart will be updated.
    fun onReload() = viewModelScope.launch {
        Log.i("ShoppingCart", "Fetch new data from the shopping cart")
        _meals.value = DataState.Success(receiveNewData((paymentRepository)))
    }

    // If an item was clicked to delete, remove the item and reload the data
    fun onDeleteClick(id: String) = viewModelScope.launch {
        paymentRepository.removeItemFromShoppingCart(id)
        onReload()
    }


    // Create the payment order and sent it to the home view-model
    fun onPaymentClick(): CreateOrderRequest {
        return CreateOrderRequest(
            paymentRepository.shoppingCart.map { CreateOrderRequestMeal(id = it.id!!, selections = it.selections) }
        )
    }




}
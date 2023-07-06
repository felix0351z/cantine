package de.juliando.app.android.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.ViewState
import de.juliando.app.models.objects.ui.Order
import de.juliando.app.repository.PaymentRepository
import de.juliando.app.utils.asDisplayable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val paymentRepository: PaymentRepository
): ViewModel() {

    private var _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val state = _state.asStateFlow()

    private var _orders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    val orders = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Try to get the orders from the paymentRepository
                val jobs = listOf(
                    launch { _orders.value =  paymentRepository.getOrders().asDisplayable() }
                )

                jobs.joinAll()
                _state.value = ViewState.Success
            } catch (ex: Exception) {
                _state.value = ViewState.Error(ex)
            }
        }
    }

    /**
     * Function to refresh the data (gets the current data from the paymentRepository)
     */
    fun refresh() = viewModelScope.launch {
            _state.value = ViewState.Loading
            _isLoading.value = true
            try {
                _orders.value = paymentRepository.getOrders().asDisplayable()
                _state.value = ViewState.Success
                _isLoading.value = false
            }catch (e: Exception){
                _state.value = ViewState.Error(e)
                _isLoading.value = false
            }
        }

    /**
     * Deletes the order when delete is pressed (long click on an order)
     */
    fun onDeleteClick(id: String) = viewModelScope.launch {
        paymentRepository.deleteOrder(id)
        refresh()
    }
}
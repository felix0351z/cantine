package de.juliando.app.android.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.ViewState
import de.juliando.app.models.objects.ui.Order
import de.juliando.app.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class OrderViewModel(
    private val paymentRepository: PaymentRepository
): ViewModel() {

    private var _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val state = _state.asStateFlow()

    private var _orders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    val orders = _orders.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jobs = listOf(
                    launch { _orders.value =  paymentRepository.getOrders() }
                )

                jobs.joinAll()
                _state.value = ViewState.Success
            } catch (ex: Exception) {
                _state.value = ViewState.Error(ex)
            }


        }
    }
}
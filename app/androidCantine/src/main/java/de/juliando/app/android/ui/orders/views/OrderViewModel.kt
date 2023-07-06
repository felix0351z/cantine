package de.juliando.app.android.ui.orders.views

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    private val _order =  MutableStateFlow<DataState>(DataState.Loading)
    val order = _order.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                // try to get the order from the server
                _order.value = DataState.Success(paymentRepository.getOrder(orderId))
            } catch (ex: Exception) {
                // catch show an error message
                Log.e(this@OrderViewModel::class.toString(), "Error occurred while loading the clicked order", ex)
                _order.value = DataState.Error(ex)
            }
        }
    }

    /**
     * @return orderId to create the qrcode
     */
    fun getOrderId(): String{
        return orderId
    }
}
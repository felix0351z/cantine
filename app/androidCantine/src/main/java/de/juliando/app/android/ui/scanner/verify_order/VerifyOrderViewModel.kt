package de.juliando.app.android.ui.scanner.verify_order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.backend.VerifyOrderRequest
import de.juliando.app.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VerifyOrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val paymentRepository: PaymentRepository
): ViewModel() {
    private val orderID: String = checkNotNull(savedStateHandle["order"])

    private var _order = MutableStateFlow<DataState>(DataState.Loading)
    val order = _order.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val verifyOrderRequest = VerifyOrderRequest(
                    orderId = orderID
                )
                _order.value = DataState.Success(paymentRepository.verifyOrder(verifyOrderRequest))
            } catch (ex: Exception){
                _order.value = DataState.Error(ex)
            }
        }
    }
}
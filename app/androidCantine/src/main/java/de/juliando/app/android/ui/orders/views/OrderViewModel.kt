package de.juliando.app.android.ui.orders.views

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class OrderViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    fun getOrderId(): String{
        return orderId
    }
}
package de.juliando.app.android.ui.home.shopping_cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingCartViewModel(
    contentRepository: ContentRepository
) : ViewModel() {


    private val _meals = MutableStateFlow<DataState>(DataState.Loading)
    val meals = _meals.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                //TODO: Do call to the contentRepository and get all current elements in the shopping cart
                _meals.value = DataState.Success(emptyList<Meal>())
            } catch (ex: Exception) {
                Log.e(this@ShoppingCartViewModel::class.toString(), "Error occurred while loading the shopping cart", ex)
                _meals.value = DataState.Error(ex)
            }
        }
    }


    fun onPaymentClick() {
        TODO()
    }




}
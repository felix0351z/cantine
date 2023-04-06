package de.juliando.app.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.ui.utils.DataState
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.repository.AuthenticationRepository
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

fun List<Meal>.selectCategories() = this.filter { it.category != null }.map { it.category!! }.distinct()
fun List<Meal>.selectWithCategory(category: String) = this.filter { it.category == category }


class HomeViewModel(
    contentRepository: ContentRepository,
    authenticationRepository: AuthenticationRepository
): ViewModel() {


    private var _meals: List<Meal> = emptyList()

    private var _posts: MutableStateFlow<DataState> = MutableStateFlow(DataState.Loading)
    val posts = _posts.asStateFlow()

    private var _selectedMeals: MutableStateFlow<DataState> = MutableStateFlow(DataState.Loading)
    val selectedMeals = _selectedMeals.asStateFlow()

    private var _categories: MutableStateFlow<DataState> = MutableStateFlow(DataState.Loading)
    val categories = _categories


    init {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.login()

            // Get the reports and meals asynchronously
            try {
                launch {
                    _posts.value = DataState.Success(contentRepository.getReports())
                }

                launch {
                    // For the first time, select all meals

                    _meals = contentRepository.getMeals()
                    _categories.value = DataState.Success(_meals.selectCategories())
                    _selectedMeals.value = DataState.Success(_meals)
                }

            } catch (ex: Exception) {
                //TODO: Handle errors which are able to

            }


        }
    }

    fun updateMealSelection(category: String?) {

    }

}
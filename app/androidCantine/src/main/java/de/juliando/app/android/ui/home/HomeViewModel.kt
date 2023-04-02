package de.juliando.app.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.models.objects.ui.Report
import de.juliando.app.repository.AuthenticationRepository
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeDataState {
    object Loading: HomeDataState()
    class Success(val reports: List<Report>, val meals: List<Meal> ): HomeDataState()
    class Error(val exception: Exception): HomeDataState()

}

class HomeViewModel(
    contentRepository: ContentRepository,
    authenticationRepository: AuthenticationRepository
): ViewModel() {

    // Collect the reports and meals together
    private var _state: MutableStateFlow<HomeDataState> = MutableStateFlow(HomeDataState.Loading)
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.login()

            // Get the reports and meals asynchronously
            try {
                val reports = async { contentRepository.getReports() }
                val meals = async { contentRepository.getMeals() }

                _state.value = HomeDataState.Success(
                    reports = reports.await(),
                    meals = meals.await()
                )
            } catch (ex: Exception) {
                //TODO: Handle errors which are able to

                HomeDataState.Error(ex)
            }


        }
    }

}
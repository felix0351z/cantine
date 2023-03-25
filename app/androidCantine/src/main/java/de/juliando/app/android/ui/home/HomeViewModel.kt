package de.juliando.app.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.ui.utils.DataState
import de.juliando.app.repository.AuthenticationRepository
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    contentRepository: ContentRepository,
    authenticationRepository: AuthenticationRepository
): ViewModel() {

    private var _reports: MutableStateFlow<DataState> = MutableStateFlow(DataState.Loading)
    val reports = _reports.asStateFlow()

    private var _meals: MutableStateFlow<DataState> = MutableStateFlow(DataState.Loading)
    val meals = _meals.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.login()

            // Load reports
            launch {
                try {
                    val reports = contentRepository.getReports()
                    _reports.value = DataState.Success(reports)
                } catch (ex: Exception) {
                    _reports.value = DataState.Error(ex)
                    //TODO: Handle errors which are able to
                }
            }
            // Load meals
            launch {
                try {
                    val meals = contentRepository.getMeals()
                    _meals.value = DataState.Success(meals)
                } catch (ex: Exception) {
                    _meals.value = DataState.Error(ex)
                    //TODO: Handle errors which are able to
                }
            }

        }
    }

}
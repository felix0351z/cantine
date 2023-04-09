package de.juliando.app.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.models.objects.ui.Report
import de.juliando.app.repository.AuthenticationRepository
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

const val TAG = "HomeViewModel"

fun List<Meal>.selectCategories() = this.filter { it.category != null }.map { it.category!! }.distinct()
fun List<Meal>.selectWithCategory(category: String) = this.filter { it.category == category }

sealed class ViewState {
    object Loading: ViewState()
    object Success: ViewState()
    class Error(exception: Exception): ViewState()
}



class HomeViewModel(
    contentRepository: ContentRepository,
    authenticationRepository: AuthenticationRepository
): ViewModel() {

    private var _searchInput: MutableStateFlow<String> = MutableStateFlow("")
    val searchInput = _searchInput.asStateFlow()


    private var _selectedCategory: MutableStateFlow<String?> = MutableStateFlow(null)
    private var _selectedTags: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())

    private var _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val state = _state.asStateFlow()

    private var _posts: MutableStateFlow<List<Report>> = MutableStateFlow(emptyList())
    val posts = _posts.asStateFlow()

    private var _meals: MutableStateFlow<List<Meal>> = MutableStateFlow(emptyList())

    val selectedMeals = _meals
        // Filter after category
        .combine(_selectedCategory) { meals, category ->
            if (category == null) meals
            else meals.selectWithCategory(category)
        }
        // Map for tags (Not included yet)
        /*.combine(_selectedTags) { meals, tags ->
            if (tags.isEmpty()) meals
            else meals.filter {}
        }*/
        // Filter after search request
        .combine(_searchInput) { meals, searchQuery ->
            if (searchQuery.isBlank()) meals
            else meals.filter { it.matchQuery(searchQuery) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(4000),
            initialValue = _meals.value
        )

    val categories = _meals
        .map { it.selectCategories() }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(4000),
        initialValue = emptyList()
    )
    //val tags = _meals todo!


    init {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.login()

            try {
                val jobs = listOf(
                    launch { _posts.value =  contentRepository.getReports() },
                    launch { _meals.value =  contentRepository.getMeals() }
                )

                jobs.joinAll()
                _state.value = ViewState.Success
            } catch (ex: Exception) {
                //TODO: Handle errors which are able to
                _state.value = ViewState.Error(ex)
            }


        }
    }

    fun updateCategorySelection(category: String?) {
        Log.d(TAG, "Selected category tab $category")
        _selectedCategory.value = category
    }

    fun addTag() {

    }
    fun removeTag() {

    }

    fun updateSearchText(text: String) {
        _searchInput.value = text
    }

}
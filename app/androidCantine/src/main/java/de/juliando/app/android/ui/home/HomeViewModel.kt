package de.juliando.app.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.ui.utils.ViewState
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

class HomeViewModel(
    contentRepository: ContentRepository,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {

    // Save the selected tags
    private val savedTags = mutableListOf<String>()

    private var _searchInput: MutableStateFlow<String> = MutableStateFlow("")
    val searchInput = _searchInput.asStateFlow()

    private var _selectedCategory: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private var _selectedTags: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val selectedTags = _selectedTags.asStateFlow()

    private var _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val state = _state.asStateFlow()

    private var _posts: MutableStateFlow<List<Report>> = MutableStateFlow(emptyList())
    val posts = _posts.asStateFlow()

    private var _meals: MutableStateFlow<List<Meal>> = MutableStateFlow(emptyList())

    val categories = _meals
        .map { it.selectCategories() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(4000),
            initialValue = emptyList()
        )
    val tags = _meals.map { it.flatMap { it.tags } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(4000),
            initialValue = emptyList()
        )

    val selectedMeals = _meals
        // Filter after category
        .combine(_selectedCategory) { meals, category ->
            if (category == null) meals
            else meals.selectWithCategory(category)
        }
        // Map for tags
        .combine(_selectedTags) { meals, tags ->
            if (tags.isEmpty()) meals
            else meals.filter {
                it.tags.any { tags.contains(it) }
            }
        }
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


    init {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun updateTagEntries(tag: String) {
        if (savedTags.contains(tag)) savedTags.remove(tag)
        else savedTags.add(tag)

        // Kotlin StateFlows are only notifying it's observers if the actual value has changed. (The list won't change of course)
        // So a workaround is needed with the map function.
        _selectedTags.value = savedTags.map { it }
    }

    fun updateSearchText(text: String) {
        _searchInput.value = text
    }

    suspend fun logout(){
        authenticationRepository.logout()
    }
}
package de.juliando.app.android.ui.home.views

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.repository.ContentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    savedStateHandle: SavedStateHandle,
    private val contentRepository: ContentRepository
) : ViewModel() {


    private val reportId: String = checkNotNull(savedStateHandle["reportId"])

    private val _report =  MutableStateFlow<DataState>(DataState.Loading)
    val report = _report.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                delay(1000)
                _report.value = DataState.Success(contentRepository.getReport(reportId))
            } catch (ex: Exception) {
                Log.i("ReportViewModel", "Error occured while loading", ex)
                _report.value = DataState.Error(ex)
                //TODO: Handle errors
            }
        }
    }





}
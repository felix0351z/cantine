package de.juliando.app.android.ui.home.report

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.juliando.app.android.utils.DataState
import de.juliando.app.repository.ContentRepository
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
                _report.value = DataState.Success(contentRepository.getReport(reportId))
            } catch (ex: Exception) {
                Log.e(this@ReportViewModel::class.toString(), "Error occurred while loading the clicked report", ex)
                _report.value = DataState.Error(ex)
            }
        }
    }



}
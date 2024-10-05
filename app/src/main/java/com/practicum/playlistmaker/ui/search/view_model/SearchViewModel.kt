package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.interactor.SearchInteractor
import com.practicum.playlistmaker.domain.search.interactor.TracksStorageInteractor
import com.practicum.playlistmaker.presentation.state.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val trackStorageInteractor: TracksStorageInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val state = MutableLiveData<ScreenState>()

    fun getState(): LiveData<ScreenState> = state

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText
        clearJob()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            loadData(changedText)
        }
    }

    fun clearJob() {
        searchJob?.cancel()
    }

    fun loadData(expression: String) {
        state.value = ScreenState.Loading

        viewModelScope.launch {
            searchInteractor
                .search(expression)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(data: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (data != null) {
            tracks.addAll(data)
        }
        when {
            errorMessage != null -> {
                val error = ScreenState.Error("Ошибка со связью")
                state.postValue(error)
            }

            tracks.isEmpty() -> {
                val empty = ScreenState.Empty("Ничего не нашлось")
                state.postValue(empty)
            }

            else -> {
                val content = ScreenState.Content(tracks)
                state.postValue(content)
            }
        }
    }


    fun loadHistory(): List<Track> {
        return trackStorageInteractor.getTrack()
    }

    fun saveHistory(list: List<Track>): List<Track> {
        return trackStorageInteractor.saveTracks(list)
    }
}
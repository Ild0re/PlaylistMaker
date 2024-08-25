package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.interactor.SearchInteractor
import com.practicum.playlistmaker.domain.search.interactor.TracksStorageInteractor
import com.practicum.playlistmaker.presentation.state.ScreenState

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val trackStorageInteractor: TracksStorageInteractor
) : ViewModel() {
    private val state = MutableLiveData<ScreenState>()

    fun getState(): LiveData<ScreenState> = state

    fun loadData(expression: String) {
        state.value = ScreenState.Loading

        searchInteractor.search(
            expression,
            object : Consumer<List<Track>> {
                override fun consume(data: List<Track>?, errorMessage: String?) {
                    val tracks = mutableListOf<Track>()
                    if (data != null) {
                        tracks.addAll(data)
                        val content = ScreenState.Content(data)
                        state.postValue(content)
                        if (tracks.isEmpty()) {
                            val empty =
                                ScreenState.Empty("Ничего не нашлось")
                            state.postValue(empty)
                        }
                    } else if (errorMessage != null) {
                        val error =
                            ScreenState.Error("Ошибка со связью")
                        state.postValue(error)
                    }
                }
            })
    }

    fun loadHistory(): List<Track> {
        return trackStorageInteractor.getTrack()
    }

    fun saveHistory(list: List<Track>): List<Track> {
        return trackStorageInteractor.saveTracks(list)
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(Creator.provideTracksSearchUseCase(),
                        Creator.provideTracksStorageUseCase())
                }
            }
        }
    }
}
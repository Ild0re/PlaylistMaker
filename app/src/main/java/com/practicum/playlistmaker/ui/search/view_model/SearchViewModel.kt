package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.state.ScreenState

class SearchViewModel : ViewModel() {
    private val getSearchInteractor = Creator.provideTracksSearchUseCase()

    private val state = MutableLiveData<ScreenState>()

    fun getState(): LiveData<ScreenState> = state

    fun loadData(expression: String) {
        state.value = ScreenState.Loading

        getSearchInteractor.search(
            expression,
            object : Consumer<List<Track>> {
                override fun consume(data: List<Track>) {
                    if (data.isNotEmpty() && data != null) {
                        val content = ScreenState.Content(data)
                        state.value = content
                    } else if (data.isEmpty()) {
                        val error =
                            ScreenState.Empty(Creator.application.getString(R.string.nothing_to_show))
                        state.postValue(error)
                    } else {
                        val error =
                            ScreenState.Error(Creator.application.getString(R.string.internet_issue))
                        state.postValue(error)
                    }
                }
            }
        )
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel()
                }
            }
        }
    }
}
package com.practicum.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.favourites.FavouritesInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.state.FavouritesState
import kotlinx.coroutines.launch

class FavouritesViewModel(private val interactor: FavouritesInteractor) : ViewModel() {

    private val favouriteState = MutableLiveData<FavouritesState>()

    val observeState: LiveData<FavouritesState> = favouriteState

    fun loadData() {
        viewModelScope.launch {
            interactor.getTracks()
                .collect { tracks -> processResult(tracks) }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavouritesState.Empty("empty"))
        } else {
            renderState(FavouritesState.Content(tracks))
        }
    }

    private fun renderState(state: FavouritesState) {
        favouriteState.postValue(state)
    }
}
package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.models.Track

sealed interface FavouritesState {

    data class Content(
        val tracks: List<Track>
    ) : FavouritesState

    data class Empty(
        val message: String
    ) : FavouritesState
}
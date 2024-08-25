package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.models.Track

sealed interface ScreenState {
    object Loading : ScreenState
    data class Empty(val message: String) : ScreenState
    data class Error(val message: String) : ScreenState
    data class Content(val data: List<Track>) : ScreenState
}
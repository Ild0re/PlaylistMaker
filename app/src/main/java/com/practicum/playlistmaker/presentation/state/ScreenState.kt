package com.practicum.playlistmaker.presentation.state

sealed interface ScreenState {
    object Loading: ScreenState
    data class Error(val message: String): ScreenState
    data class Content<T>(val data: T): ScreenState
}
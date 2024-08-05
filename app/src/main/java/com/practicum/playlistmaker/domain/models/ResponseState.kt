package com.practicum.playlistmaker.domain.models

sealed interface ResponseState<T> {
    data class Success<T>(val data:T) : ResponseState<T>
    data class Error<T>(val message: String) : ResponseState<T>
}
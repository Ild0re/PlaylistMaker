package com.practicum.playlistmaker.ui.track.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.track.OnCompletionListener
import com.practicum.playlistmaker.domain.track.OnPreparedListener
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor

class TrackViewModel(private val interactor: MediaPlayerInteractor) : ViewModel() {

    fun prepare(string: String) {
        interactor.prepare(string)
    }

    fun start() {
        interactor.start()
    }

    fun pause() {
        interactor.pause()
    }

    fun reset() {
        interactor.reset()
    }

    fun getCurrentPosition(): Int {
        return interactor.getCurrentPosition()
    }

    fun setOnPreparedListener(listener: OnPreparedListener) {
        interactor.setOnPreparedListener(listener)
    }

    fun setOnCompletionListener(listener: OnCompletionListener) {
        interactor.setOnComplitionListener(listener)
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    TrackViewModel(Creator.provideMediaPlayerUseCase())
                }
            }
        }
    }
}

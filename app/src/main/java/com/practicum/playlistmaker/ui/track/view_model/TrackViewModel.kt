package com.practicum.playlistmaker.ui.track.view_model

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor

class TrackViewModel(private val interactor: MediaPlayerInteractor): ViewModel() {
    val mediaPlayer = MediaPlayer()

    fun prepare(string: String) {
        interactor.prepare(string)
        mediaPlayer.setDataSource(string)
        mediaPlayer.prepareAsync()
    }

    fun start() {
        interactor.start()
        mediaPlayer.start()
    }

    fun pause() {
        interactor.pause()
        mediaPlayer.pause()
    }

    fun release() {
        interactor.release()
        mediaPlayer.release()
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

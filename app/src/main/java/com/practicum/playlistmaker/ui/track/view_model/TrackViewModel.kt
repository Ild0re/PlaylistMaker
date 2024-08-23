package com.practicum.playlistmaker.ui.track.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class TrackViewModel: ViewModel() {
    private val getMediaPlayerIteractor = Creator.provideMediaPlayerUseCase()

    fun prepare(string: String) {
        return getMediaPlayerIteractor.prepare(string)
    }

    fun start() {
        return getMediaPlayerIteractor.start()
    }

    fun pause() {
        return getMediaPlayerIteractor.pause()
    }

    fun release() {
        return getMediaPlayerIteractor.release()
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    TrackViewModel()
                }
            }
        }
    }
}

package com.practicum.playlistmaker.domain.track.impl

import com.practicum.playlistmaker.data.track.MediaPlayerRepository
import com.practicum.playlistmaker.domain.track.OnCompletionListener
import com.practicum.playlistmaker.domain.track.OnPreparedListener
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) :
    MediaPlayerInteractor {
    override fun prepare(src: String) {
        return repository.prepare(src)
    }

    override fun start() {
        return repository.start()
    }

    override fun pause() {
        return repository.pause()
    }

    override fun reset() {
        return repository.reset()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        return repository.setOnPreparedListener(listener)
    }

    override fun setOnComplitionListener(listener: OnCompletionListener) {
        return repository.setOnComplitionListener(listener)
    }
}
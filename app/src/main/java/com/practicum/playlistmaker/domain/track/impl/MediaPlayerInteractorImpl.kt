package com.practicum.playlistmaker.domain.track.impl

import com.practicum.playlistmaker.data.track.MediaPlayerRepository
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository):
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

    override fun release() {
        return repository.release()
    }
}
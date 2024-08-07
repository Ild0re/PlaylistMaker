package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.domain.use_cases.MediaPlayerInteractor

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository): MediaPlayerInteractor {
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
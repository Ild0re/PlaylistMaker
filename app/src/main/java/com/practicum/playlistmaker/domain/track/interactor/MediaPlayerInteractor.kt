package com.practicum.playlistmaker.domain.track.interactor

interface MediaPlayerInteractor {
    fun prepare(src: String)

    fun start()

    fun pause()

    fun release()
}
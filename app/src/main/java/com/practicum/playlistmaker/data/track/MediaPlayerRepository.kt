package com.practicum.playlistmaker.data.track

interface MediaPlayerRepository {

    fun prepare(src: String)

    fun start()

    fun pause()

    fun release()
}
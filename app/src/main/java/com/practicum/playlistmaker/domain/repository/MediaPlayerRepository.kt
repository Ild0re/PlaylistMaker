package com.practicum.playlistmaker.domain.repository

import android.media.MediaPlayer

interface MediaPlayerRepository {

    fun prepare(src: String)

    fun start()

    fun pause()

    fun release()
}
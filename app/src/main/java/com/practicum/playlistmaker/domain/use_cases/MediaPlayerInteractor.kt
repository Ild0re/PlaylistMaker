package com.practicum.playlistmaker.domain.use_cases

import android.media.MediaPlayer

interface MediaPlayerInteractor {
    fun prepare(src: String)

    fun start()

    fun pause()

    fun release()
}
package com.practicum.playlistmaker.domain.track.interactor

import com.practicum.playlistmaker.domain.track.OnCompletionListener
import com.practicum.playlistmaker.domain.track.OnPreparedListener

interface MediaPlayerInteractor {
    fun prepare(src: String)

    fun start()

    fun pause()

    fun getCurrentPosition(): Int

    fun setOnPreparedListener(listener: OnPreparedListener)

    fun setOnComplitionListener(listener: OnCompletionListener)

    fun reset()
}
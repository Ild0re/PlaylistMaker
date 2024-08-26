package com.practicum.playlistmaker.data.track

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.track.OnCompletionListener
import com.practicum.playlistmaker.domain.track.OnPreparedListener

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    override fun prepare(src: String) {
        mediaPlayer.setDataSource(src)
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        mediaPlayer.setOnPreparedListener { listener.onPrepared() }
    }

    override fun setOnComplitionListener(listener: OnCompletionListener) {
        mediaPlayer.setOnCompletionListener { listener.onCompletion() }
    }
}
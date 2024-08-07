package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(mediaSource: MediaPlayer): MediaPlayerRepository {

    val mediaPlayer = mediaSource

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

    override fun release() {
        mediaPlayer.release()
    }
}
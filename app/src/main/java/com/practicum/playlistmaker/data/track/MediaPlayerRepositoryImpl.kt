package com.practicum.playlistmaker.data.track

import android.media.MediaPlayer

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
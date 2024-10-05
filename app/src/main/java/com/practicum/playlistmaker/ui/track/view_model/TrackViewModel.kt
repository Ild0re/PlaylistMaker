package com.practicum.playlistmaker.ui.track.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.track.OnCompletionListener
import com.practicum.playlistmaker.domain.track.OnPreparedListener
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.presentation.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewModel(private val trackIntent: String, private val interactor: MediaPlayerInteractor) : ViewModel() {

    val url = createTracksListFromJson(trackIntent).previewUrl

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    init {
        initMediaPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun onReset() {
        resetPlayer()
    }

    fun onPlayButtonClicked() {
        when(playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }
            else -> { }
        }
    }

    private fun initMediaPlayer() {
        interactor.prepare(url)
        interactor.setOnPreparedListener(object: OnPreparedListener {
            override fun onPrepared() {
                playerState.postValue(PlayerState.Prepared())
            }
        })
        interactor.setOnCompletionListener(object: OnCompletionListener {
            override fun onCompletion() {
                timerJob?.cancel()
                playerState.postValue(PlayerState.Prepared())
            }
        })
    }

    private fun startPlayer() {
        interactor.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        interactor.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        interactor.stop()
        interactor.release()
        playerState.value = PlayerState.Default()
    }

    private fun resetPlayer() {
        interactor.reset()
        playerState.postValue(PlayerState.Prepared())
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (interactor.isPlaying()) {
                delay(300L)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(interactor.getCurrentPosition()) ?: "00:00"
    }

    private fun createTracksListFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}


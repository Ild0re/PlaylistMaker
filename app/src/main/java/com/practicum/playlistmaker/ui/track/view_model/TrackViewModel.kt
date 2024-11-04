package com.practicum.playlistmaker.ui.track.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.favourites.FavouritesInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.playlists.PlaylistInteractor
import com.practicum.playlistmaker.domain.track.OnCompletionListener
import com.practicum.playlistmaker.domain.track.OnPreparedListener
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.presentation.state.PlayerState
import com.practicum.playlistmaker.presentation.state.PlaylistsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewModel(
    private val trackIntent: String, private val interactor: MediaPlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    val track = createTracksListFromJson(trackIntent)
    private val url = track.previewUrl

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private val favouriteState = MutableLiveData<Boolean>()
    fun observeFavouriteState(): LiveData<Boolean> = favouriteState
    private val playlistsState = MutableLiveData<PlaylistsState>()
    fun observePlaylistState(): LiveData<PlaylistsState> = playlistsState

    init {
        initMediaPlayer()
    }


    fun onPause() {
        pausePlayer()
    }

    fun onReset() {
        resetPlayer()
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    fun onFavouriteClicked() {
        viewModelScope.launch {
            if (track.isFavorite == false) {
                favouritesInteractor.insertTrack(track)
                track.isFavorite = true
                favouriteState.postValue(true)
            } else {
                favouritesInteractor.deleteTrack(track)
                track.isFavorite = false
                favouriteState.postValue(false)
            }
        }
    }

    fun favouriteCheck() {
        if (track.isFavorite == false) {
            favouriteState.postValue(false)
        } else {
            favouriteState.postValue(true)
        }
    }

    private fun initMediaPlayer() {
        interactor.prepare(url)
        interactor.setOnPreparedListener(object : OnPreparedListener {
            override fun onPrepared() {
                playerState.postValue(PlayerState.Prepared())
            }
        })
        interactor.setOnCompletionListener(object : OnCompletionListener {
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
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(interactor.getCurrentPosition()) ?: "00:00"
    }

    private fun createTracksListFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }

    fun loadData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { playlists -> processResult(playlists) }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistsState.Empty("empty"))
        } else {
            renderState(PlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistsState) {
        playlistsState.postValue(state)
    }

    fun checkTrackInPlaylist(playlist: Playlist): Boolean {
        if (track.trackId in playlist.trackList) {
            return false
        } else {
            return true
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        if (checkTrackInPlaylist(playlist)  == true) {
            viewModelScope.launch {
                playlistInteractor.updatePlaylist(track, playlist)
                playlistInteractor.insertTrackToPlaylist(track)
            }
        }
    }
}


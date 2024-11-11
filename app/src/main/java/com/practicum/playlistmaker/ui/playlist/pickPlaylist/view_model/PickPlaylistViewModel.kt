package com.practicum.playlistmaker.ui.playlist.pickPlaylist.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.playlists.PlaylistInteractor
import com.practicum.playlistmaker.presentation.state.PlaylistState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PickPlaylistViewModel(val id: Int, private val interactor: PlaylistInteractor) :
    ViewModel() {

    companion object {
        const val DELAY_TO_SEND = 200L
    }

    private val playlistState = MutableLiveData<PlaylistState>()
    private val trackList = MutableStateFlow<List<Track>>(emptyList())

    val observeState: LiveData<PlaylistState> = playlistState
    val tracksFromPlaylist: StateFlow<List<Track>> = trackList.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            interactor.getPlaylistById(id)
                .collect { playlist -> processResult(playlist) }
        }
    }

    private fun processResult(playlist: Playlist) {
        renderState(PlaylistState.Content(playlist))
    }

    private fun renderState(state: PlaylistState) {
        playlistState.postValue(state)
    }

    fun getTracksFromPlaylists(list: List<Int>) {
        viewModelScope.launch {
            interactor.getTracksInPlaylist(list)
                .collect { tracks -> trackList.value = tracks }
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(track, id)
            loadData()
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            interactor.deletePlaylist(id)
        }
    }

    suspend fun sharePlaylist(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val currentPlaylist = interactor.getPlaylistById(id).first()
        interactor.getTracksInPlaylist(currentPlaylist.trackList).collect { tracks ->
            val text = buildText(currentPlaylist, tracks)
            delay(DELAY_TO_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser =
            Intent.createChooser(shareIntent, "Выберите приложение для отправки")
        return chooser
    }

    private fun buildText(playlist: Playlist, list: List<Track>): String {
        return "${playlist.trackCount} ${getEndingString(playlist.trackCount)}\n" +
                list.withIndex().joinToString("\n") { (index, track) ->
                    "${index + 1}. ${track.artistName} - ${track.trackName} (${
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)
                    })"
                }

    }

    private fun getEndingString(number: Int): String {
        val lastDigit = number % 10
        when (lastDigit) {
            1 -> return "трек"
            2, 3, 4 -> return "трека"
            else -> return "треков"
        }
    }
}
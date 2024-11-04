package com.practicum.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.insertPlaylist(playlist)
        }
    }
}
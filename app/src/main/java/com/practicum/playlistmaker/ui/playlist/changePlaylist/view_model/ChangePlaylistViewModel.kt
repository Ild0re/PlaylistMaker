package com.practicum.playlistmaker.ui.playlist.changePlaylist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.playlists.PlaylistInteractor
import com.practicum.playlistmaker.presentation.state.PlaylistState
import com.practicum.playlistmaker.ui.playlist.createPlaylist.activity.view_model.CreatePlaylistViewModel
import kotlinx.coroutines.launch

class ChangePlaylistViewModel(private val id: Int, private val interactor: PlaylistInteractor) :
    CreatePlaylistViewModel(interactor) {

    private val playlistState = MutableLiveData<PlaylistState>()

    val observeState: LiveData<PlaylistState> = playlistState

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

    fun editPlaylist(name: String, description: String, image: String) {
        viewModelScope.launch {
            interactor.getPlaylistById(id)
                .collect { playlist ->
                    interactor.editPlaylist(
                        playlist.copy(
                            id,
                            name,
                            description,
                            image,
                            playlist.trackList,
                            playlist.trackCount
                        )
                    )
                }
        }
    }
}
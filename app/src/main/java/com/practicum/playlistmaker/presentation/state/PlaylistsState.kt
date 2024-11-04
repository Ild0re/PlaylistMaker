package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.models.Playlist

interface PlaylistsState {

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    data class Empty(
        val message: String
    ) : PlaylistsState
}
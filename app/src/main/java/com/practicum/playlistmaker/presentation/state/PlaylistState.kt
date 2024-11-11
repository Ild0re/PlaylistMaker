package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {

    data class Content(
        val playlist: Playlist
    ) : PlaylistState
}
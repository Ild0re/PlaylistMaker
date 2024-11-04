package com.practicum.playlistmaker.domain.playlists

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(track: Track, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun insertTrackToPlaylist(track: Track)
}
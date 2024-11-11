package com.practicum.playlistmaker.domain.playlists

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(track: Track, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun insertTrackToPlaylist(track: Track)

    suspend fun getPlaylistById(id: Int): Flow<Playlist>

    suspend fun getTracksInPlaylist(list: List<Int>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(track: Track, id: Int)

    suspend fun deletePlaylist(id: Int)

    suspend fun editPlaylist(playlist: Playlist)
}
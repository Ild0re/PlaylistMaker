package com.practicum.playlistmaker.domain.playlists

import com.practicum.playlistmaker.data.playlists.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        return repository.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        return repository.updatePlaylist(track, playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun insertTrackToPlaylist(track: Track) {
        return repository.insertTrackToPlaylist(track)
    }
}
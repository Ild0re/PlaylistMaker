package com.practicum.playlistmaker.data.playlists

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistDbConvertor
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.data.db.TracksInPlaylistsEntity
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlaylistDbConvertor
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        val playlistEntity = converter.mapToEntity(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        playlist.trackList.add(track.trackId)
        val updatedList = playlist.copy(trackCount = playlist.trackCount + 1)
        val playlistEntity = converter.mapToEntity(updatedList)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlistEntity = appDatabase.playlistDao().getPlaylists()
        val playlists = convertFromPlaylistEntity(playlistEntity)
        emit(playlists)
    }

    override suspend fun insertTrackToPlaylist(track: Track) {
        val trackToPlaylistEntity = TracksInPlaylistsEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.country,
            track.primaryGenreName,
            track.previewUrl,
            track.isFavorite,
            System.currentTimeMillis()
        )
        appDatabase.tracksInPlaylistsDao().insertTrackToPlaylist(trackToPlaylistEntity)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> converter.mapToPlaylist(playlist) }
    }
}
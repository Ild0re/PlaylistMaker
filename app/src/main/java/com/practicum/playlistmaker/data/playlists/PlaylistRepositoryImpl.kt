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
) : PlaylistRepository {
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
        val trackToPlaylistEntity = mapToEntity(track)
        appDatabase.tracksInPlaylistsDao().insertTrackToPlaylist(trackToPlaylistEntity)
    }

    override suspend fun getPlaylistById(id: Int): Flow<Playlist> = flow {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(id)
        val playlist = converter.mapToPlaylist(playlistEntity)
        emit(playlist)
    }

    override suspend fun getTracksInPlaylist(list: List<Int>): Flow<List<Track>> = flow {
        val trackListEntity = appDatabase.tracksInPlaylistsDao().getTrackInPlaylists()
        val newList = mutableListOf<Track>()
        for (i in list) {
            for (x in trackListEntity) {
                if (i == x.trackId) {
                    newList.add(mapToTrack(x))
                }
            }
        }
        emit(newList)
    }

    override suspend fun deleteTrack(track: Track) {
        val tracksInPlaylistsEntity = mapToEntity(track)
        appDatabase.tracksInPlaylistsDao().deleteTrack(tracksInPlaylistsEntity)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, id: Int) {
        val currentPlaylist = appDatabase.playlistDao().getPlaylistById(id)
        val playlist = converter.mapToPlaylist(currentPlaylist)

        playlist.trackList.remove(track.trackId)
        val updatedList = playlist.copy(trackCount = playlist.trackCount - 1)
        val playlistEntity = converter.mapToEntity(updatedList)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
        checkTrackInPlaylists(track)
    }

    override suspend fun deletePlaylist(id: Int) {
        val currentPlaylist = appDatabase.playlistDao().getPlaylistById(id)
        val playlist = converter.mapToPlaylist(currentPlaylist)
        val trackListEntity = appDatabase.tracksInPlaylistsDao().getTrackInPlaylists()
        val trackList = trackListEntity.map { track -> mapToTrack(track)}
        val iterator = playlist.trackList.iterator()
        while (iterator.hasNext()) {
            val trackId = iterator.next()
            if (trackList.any { it.trackId == trackId }) {
                iterator.remove()
            }
        }
        val playlistEntity = converter.mapToEntity(playlist)
        appDatabase.playlistDao().deletePlaylist(playlistEntity)
        for (i in trackList) {
            checkTrackInPlaylists(i)
        }
    }

    override suspend fun editPlaylist(playlist: Playlist) {
        val playlistEntity = converter.mapToEntity(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> converter.mapToPlaylist(playlist) }
    }

    private fun mapToTrack(tracksInPlaylistsEntity: TracksInPlaylistsEntity): Track {
        return Track(
            tracksInPlaylistsEntity.trackId,
            tracksInPlaylistsEntity.trackName,
            tracksInPlaylistsEntity.artistName,
            tracksInPlaylistsEntity.trackTimeMillis,
            tracksInPlaylistsEntity.artworkUrl100,
            tracksInPlaylistsEntity.collectionName,
            tracksInPlaylistsEntity.releaseDate,
            tracksInPlaylistsEntity.country,
            tracksInPlaylistsEntity.primaryGenreName,
            tracksInPlaylistsEntity.previewUrl
        )
    }

    private fun mapToEntity(track: Track): TracksInPlaylistsEntity {
        return TracksInPlaylistsEntity(
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
    }

    private suspend fun checkTrackInPlaylists(track: Track) {
        val allPlaylistsEntities = appDatabase.playlistDao().getPlaylists()
        val allPlaylists = convertFromPlaylistEntity(allPlaylistsEntities)
        var bool = false

        for (i in allPlaylists) {
            if (track.trackId in i.trackList) {
                bool = true
            }
        }

        if (bool == false) {
            deleteTrack(track)
        }
    }
}
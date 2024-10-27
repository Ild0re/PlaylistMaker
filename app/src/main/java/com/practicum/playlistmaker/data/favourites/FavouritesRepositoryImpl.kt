package com.practicum.playlistmaker.data.favourites

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackDbConvertor
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
): FavouritesRepository {
    override suspend fun insertTrack(track: Track) {
        val trackEntity = trackDbConvertor.mapToEntity(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrack(track: Track) {
        val trackEntity = trackDbConvertor.mapToEntity(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override suspend fun getTracks(): Flow<List<Track>> = flow {
        val trackEntities = appDatabase.trackDao().getTracks()
        val tracks = convertFromTrackEntity(trackEntities)
        for (i in tracks) {
            i.isFavorite = true
        }
        emit(tracks)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapToModel(track) }
    }
}
package com.practicum.playlistmaker.domain.favourites

import com.practicum.playlistmaker.data.favourites.FavouritesRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl(
    private val repository: FavouritesRepository
) : FavouritesInteractor {
    override suspend fun insertTrack(track: Track) {
        return repository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        return repository.deleteTrack(track)
    }

    override suspend fun getTracks(): Flow<List<Track>> {
        return repository.getTracks()
    }
}
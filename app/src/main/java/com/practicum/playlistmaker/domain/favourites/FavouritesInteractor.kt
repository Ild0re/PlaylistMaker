package com.practicum.playlistmaker.domain.favourites

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    suspend fun getTracks(): Flow<List<Track>>
}
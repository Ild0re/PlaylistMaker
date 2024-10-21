package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksStorageRepository {

    fun getTracks(): Flow<List<Track>>

    fun saveTracks(historyList: List<Track>): List<Track>

}
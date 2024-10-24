package com.practicum.playlistmaker.domain.search.interactor

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksStorageInteractor {

    fun getTrack(): List<Track>

    fun getTracksFlow(data: List<Track>): Flow<List<Track>>

    fun saveTracks(trackList: List<Track>): List<Track>
}
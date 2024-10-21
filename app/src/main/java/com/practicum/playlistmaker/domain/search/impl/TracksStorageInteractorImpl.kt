package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.data.search.TracksStorageRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.interactor.TracksStorageInteractor
import kotlinx.coroutines.flow.Flow

class TracksStorageInteractorImpl(private val repository: TracksStorageRepository) :
    TracksStorageInteractor {
    override fun getTrack(): Flow<List<Track>> {
        return repository.getTracks()
    }

    override fun saveTracks(trackList: List<Track>): List<Track> {
        return repository.saveTracks(trackList)
    }

}
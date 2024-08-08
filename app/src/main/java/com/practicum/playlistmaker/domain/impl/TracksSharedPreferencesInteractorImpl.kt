package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksSharedPreferencesRepository
import com.practicum.playlistmaker.domain.use_cases.TracksSharedPreferencesInteractor

class TracksSharedPreferencesInteractorImpl(private val repository: TracksSharedPreferencesRepository) :
    TracksSharedPreferencesInteractor {
    override fun getTrack(): List<Track> {
        return repository.getTracks()
    }

    override fun saveTracks(trackList: List<Track>): List<Track> {
        return repository.saveTracks(trackList)
    }

}
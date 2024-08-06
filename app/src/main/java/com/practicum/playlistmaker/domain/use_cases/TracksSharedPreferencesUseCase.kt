package com.practicum.playlistmaker.domain.use_cases

import com.practicum.playlistmaker.domain.models.Track

interface TracksSharedPreferencesUseCase {

    fun getTrack(): List<Track>

    fun saveTracks(trackList: List<Track>): List<Track>
}
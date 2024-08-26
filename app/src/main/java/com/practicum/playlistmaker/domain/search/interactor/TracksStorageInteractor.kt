package com.practicum.playlistmaker.domain.search.interactor

import com.practicum.playlistmaker.domain.models.Track

interface TracksStorageInteractor {

    fun getTrack(): List<Track>

    fun saveTracks(trackList: List<Track>): List<Track>
}
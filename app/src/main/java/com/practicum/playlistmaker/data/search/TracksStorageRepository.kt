package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.models.Track

interface TracksStorageRepository {

    fun getTracks(): List<Track>

    fun saveTracks(historyList: List<Track>): List<Track>

}
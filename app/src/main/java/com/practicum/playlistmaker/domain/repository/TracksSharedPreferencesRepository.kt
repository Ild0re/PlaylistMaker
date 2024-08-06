package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface TracksSharedPreferencesRepository {

    fun getTracks(): List<Track>

    fun saveTracks(historyList: List<Track>): List<Track>

}
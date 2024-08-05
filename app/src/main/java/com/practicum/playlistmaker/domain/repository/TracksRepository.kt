package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.data.dto.SongDto
import com.practicum.playlistmaker.domain.models.Song
import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun search(expression: String): List<Track>
}
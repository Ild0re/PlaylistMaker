package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Song

interface TracksRepository {
    fun search(expression: String): List<Song>
}
package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchRepository {
    fun search(expression: String): List<Track>
}
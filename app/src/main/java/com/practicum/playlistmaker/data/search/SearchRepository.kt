package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.util.Resource

interface SearchRepository {
    fun search(expression: String): Resource<List<Track>>
}
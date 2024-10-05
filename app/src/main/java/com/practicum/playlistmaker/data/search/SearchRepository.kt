package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(expression: String): Flow<Resource<List<Track>>>
}
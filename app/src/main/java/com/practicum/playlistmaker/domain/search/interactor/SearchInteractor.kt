package com.practicum.playlistmaker.domain.search.interactor

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun search(expression: String): Flow<Pair<List<Track>?, String?>>
}
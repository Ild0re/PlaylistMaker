package com.practicum.playlistmaker.domain.search.interactor

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun search(expression: String, consumer: Consumer<List<Track>>)
}
package com.practicum.playlistmaker.domain.use_cases

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Song
import com.practicum.playlistmaker.domain.models.Track

interface TracksSearchUseCase {
    fun search(expression: String, consumer: Consumer<List<Track>>)
}
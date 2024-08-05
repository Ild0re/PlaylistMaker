package com.practicum.playlistmaker.domain.consumer

import com.practicum.playlistmaker.domain.models.ResponseState
import com.practicum.playlistmaker.domain.models.Song
import com.practicum.playlistmaker.domain.models.Track

interface Consumer<T> {
    fun consume(search: List<Track>)
}
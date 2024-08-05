package com.practicum.playlistmaker.domain.consumer

import com.practicum.playlistmaker.domain.models.ResponseState
import com.practicum.playlistmaker.domain.models.Song

interface Consumer<T> {
    fun consume(search: List<Song>)
}
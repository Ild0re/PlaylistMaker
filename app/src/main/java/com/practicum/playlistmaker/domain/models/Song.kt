package com.practicum.playlistmaker.domain.models

data class Song(
    val resultCount: Int,
    val results: List<Track>
)
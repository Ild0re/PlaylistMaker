package com.practicum.playlistmaker.domain.models

data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val path: String,
    val trackList: MutableList<Int>,
    val trackCount: Int
)
package com.practicum.playlistmaker.data.dto

data class SongDto(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
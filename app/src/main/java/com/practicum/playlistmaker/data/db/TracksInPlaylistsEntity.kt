package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists_table")
data class TracksInPlaylistsEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val previewUrl: String,
    val isFavourite: Boolean,
    val addedDate: Long
)
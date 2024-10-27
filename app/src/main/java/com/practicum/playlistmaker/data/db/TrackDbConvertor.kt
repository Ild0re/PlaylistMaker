package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.domain.models.Track

class TrackDbConvertor {

    fun mapToEntity(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.country,
            track.primaryGenreName,
            track.previewUrl,
            track.isFavorite,
            System.currentTimeMillis()
        )
    }

    fun mapToModel(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.country,
            track.primaryGenreName,
            track.previewUrl,
        )
    }
}
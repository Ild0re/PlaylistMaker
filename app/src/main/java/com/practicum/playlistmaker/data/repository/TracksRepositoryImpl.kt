package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.SongDto
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.models.Song
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun search(expression: String): List<Song> {
        val response = networkClient.doRequest(TracksSearchRequest(expression).toString())
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).value.map {
                Song(
                    it.resultCount,
                    it.results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.previewUrl,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.country,
                            it.primaryGenreName
                        )
                    }
                )
            }
        } else {
            return emptyList()
        }
    }
}
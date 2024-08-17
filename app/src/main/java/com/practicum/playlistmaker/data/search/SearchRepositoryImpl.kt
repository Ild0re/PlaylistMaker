package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.repository.NetworkClient
import com.practicum.playlistmaker.domain.models.Track

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override fun search(expression: String): List<Track> {
        val response = networkClient.doRequest(expression)
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.country,
                    it.primaryGenreName,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}
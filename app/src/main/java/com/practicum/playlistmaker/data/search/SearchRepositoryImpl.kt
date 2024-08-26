package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.repository.NetworkClient
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.util.Resource

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override fun search(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(expression)
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Ошибка подключения к интернету")
            }

            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
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
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}
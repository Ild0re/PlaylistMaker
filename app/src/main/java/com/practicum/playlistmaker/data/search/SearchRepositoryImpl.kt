package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.repository.NetworkClient
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override fun search(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(expression)
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Ошибка подключения к интернету"))
            }

            200 -> with(response as TracksSearchResponse) {
                val data = results.map {
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
                emit(Resource.Success(data))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
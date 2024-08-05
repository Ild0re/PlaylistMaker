package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.repository.NetworkClient

class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: String): Response {
        return try {
            val resp = RetrofitClient.api.search(dto).execute()
            val response = resp.body() ?: Response()

            response.apply { resultCode = resp.code() }
        } catch (ex: Exception) {
            Response().apply { resultCode = 400 }
        }
    }
}
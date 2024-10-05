package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: String): Response
}
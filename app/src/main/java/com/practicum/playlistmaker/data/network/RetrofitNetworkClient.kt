package com.practicum.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.repository.NetworkClient

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    override fun doRequest(dto: String): Response {
        try {
            if (isConnected() == false) {
                return Response().apply { resultCode = -1 }
            }
        val resp = RetrofitClient.api.search(dto).execute()
        val response = resp.body() ?: Response()
        return response.apply { resultCode = resp.code() }
    } catch (ex: Exception) {
            return Response().apply { resultCode = 400 }
        }
    }


//        return try {
//            val resp = RetrofitClient.api.search(dto).execute()
//            val response = resp.body() ?: Response()
//
//            response.apply { resultCode = resp.code() }
//        } catch (ex: Exception) {
//            Response().apply { resultCode = 400 }
//        }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
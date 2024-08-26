package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TrackAPI
import com.practicum.playlistmaker.data.repository.NetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<TrackAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackAPI::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("history_preferences", Context.MODE_PRIVATE)
        androidContext()
            .getSharedPreferences("dark_mode_preferences", Context.MODE_PRIVATE)
    }

    factory {
        MediaPlayer()
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext())
    }

}
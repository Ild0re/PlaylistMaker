package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.PREFERENCES
import com.practicum.playlistmaker.data.repository.ThemeSharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksSharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.ThemeSharedPreferencesInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksSearchInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksSharedPreferencesInteractorImpl
import com.practicum.playlistmaker.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.domain.repository.ThemeSharedPreferencesRepository
import com.practicum.playlistmaker.domain.repository.TracksSharedPreferencesRepository
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_cases.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.use_cases.ThemeSharedreferencesInteractor
import com.practicum.playlistmaker.domain.use_cases.TracksSearchInteractor
import com.practicum.playlistmaker.domain.use_cases.TracksSharedPreferencesInteractor

object Creator {

    val mediaPlayer = MediaPlayer()

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearchUseCase(): TracksSearchInteractor {
        return TracksSearchInteractorImpl(getTracksRepository())
    }

    private fun getTracksSharedPreferencesRepository(context: Context): TracksSharedPreferencesRepository {
        return TracksSharedPreferencesRepositoryImpl(context)
    }

    fun provideTracksSharedPreferencesUseCase(context: Context): TracksSharedPreferencesInteractor {
        return TracksSharedPreferencesInteractorImpl(getTracksSharedPreferencesRepository(context))
    }

    private fun getThemeSharedPreferencesRepository(context: Context): ThemeSharedPreferencesRepository {
        return ThemeSharedPreferencesRepositoryImpl(context)
    }

    fun provideThemeSharedPreferencesUseCase(context: Context): ThemeSharedreferencesInteractor {
        return ThemeSharedPreferencesInteractorImpl(getThemeSharedPreferencesRepository(context))
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository{
        return MediaPlayerRepositoryImpl(mediaPlayer)
    }

    fun provideMediaPlayerUseCase(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }
}
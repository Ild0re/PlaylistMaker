package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.ThemeSharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksSharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.domain.impl.ThemeSharedPreferencesUseCaseImpl
import com.practicum.playlistmaker.domain.impl.TracksSearchUseCaseImpl
import com.practicum.playlistmaker.domain.impl.TracksSharedPreferencesUseCaseImpl
import com.practicum.playlistmaker.domain.repository.ThemeSharedPreferencesRepository
import com.practicum.playlistmaker.domain.repository.TracksSharedPreferencesRepository
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_cases.ThemeSharedreferencesUseCase
import com.practicum.playlistmaker.domain.use_cases.TracksSearchUseCase
import com.practicum.playlistmaker.domain.use_cases.TracksSharedPreferencesUseCase

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearchUseCase(): TracksSearchUseCase {
        return TracksSearchUseCaseImpl(getTracksRepository())
    }

    private fun getTracksSharedPreferencesRepository(context: Context): TracksSharedPreferencesRepository {
        return TracksSharedPreferencesRepositoryImpl(context)
    }

    fun provideTracksSharedPreferencesUseCase(context: Context): TracksSharedPreferencesUseCase {
        return TracksSharedPreferencesUseCaseImpl(getTracksSharedPreferencesRepository(context))
    }

    private fun getThemeSharedPreferencesRepository(context: Context): ThemeSharedPreferencesRepository {
        return ThemeSharedPreferencesRepositoryImpl(context)
    }

    fun provideThemeSharedPreferencesUseCase(context: Context): ThemeSharedreferencesUseCase {
        return ThemeSharedPreferencesUseCaseImpl(getThemeSharedPreferencesRepository(context))
    }
}
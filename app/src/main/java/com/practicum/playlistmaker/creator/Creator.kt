package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.search.SearchRepository
import com.practicum.playlistmaker.data.search.SearchRepositoryImpl
import com.practicum.playlistmaker.data.search.TracksStorageRepository
import com.practicum.playlistmaker.data.search.TracksStorageRepositoryImpl
import com.practicum.playlistmaker.data.settings.ThemeStorageRepository
import com.practicum.playlistmaker.data.settings.ThemeStorageRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.track.MediaPlayerRepository
import com.practicum.playlistmaker.data.track.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksStorageInteractorImpl
import com.practicum.playlistmaker.domain.search.interactor.SearchInteractor
import com.practicum.playlistmaker.domain.search.interactor.TracksStorageInteractor
import com.practicum.playlistmaker.domain.settings.impl.ThemeStorageInteractorImpl
import com.practicum.playlistmaker.domain.settings.interactor.ThemeStorageInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.domain.sharing.interactor.SharingInteractor
import com.practicum.playlistmaker.domain.track.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.track.interactor.MediaPlayerInteractor

object Creator {

    private const val PREFERENCES = "dark_mode_preferences"
    private const val HISTORY_PREFERENCES = "history_preferences"

    private lateinit var application: Application
    private lateinit var mediaPlayer: MediaPlayer

    fun initApplication(context: Context) {
        application = context as Application
        mediaPlayer = MediaPlayer()
    }

    private fun getTracksRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient(application))
    }

    fun provideTracksSearchUseCase(): SearchInteractor {
        return SearchInteractorImpl(getTracksRepository())
    }

    private fun getTracksStorageRepository(): TracksStorageRepository {
        return TracksStorageRepositoryImpl(application.getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE))
    }

    fun provideTracksStorageUseCase(): TracksStorageInteractor {
        return TracksStorageInteractorImpl(getTracksStorageRepository())
    }

    private fun getThemeStorageRepository(): ThemeStorageRepository {
        return ThemeStorageRepositoryImpl(application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE))
    }

    fun provideThemeStorageUseCase(): ThemeStorageInteractor {
        return ThemeStorageInteractorImpl(getThemeStorageRepository())
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(mediaPlayer)
    }

    fun provideMediaPlayerUseCase(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
}
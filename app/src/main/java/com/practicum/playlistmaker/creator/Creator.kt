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

    lateinit var application: Application

    val mediaPlayer = MediaPlayer()

    fun initApplication(context: Context) {
        application = context as Application
    }

    private fun getTracksRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearchUseCase(): SearchInteractor {
        return SearchInteractorImpl(getTracksRepository())
    }

    private fun getTracksStorageRepository(context: Context): TracksStorageRepository {
        return TracksStorageRepositoryImpl(context)
    }

    fun provideTracksStorageUseCase(context: Context): TracksStorageInteractor {
        return TracksStorageInteractorImpl(getTracksStorageRepository(context))
    }

    private fun getThemeStorageRepository(context: Context): ThemeStorageRepository {
        return ThemeStorageRepositoryImpl(context)
    }

    fun provideThemeStorageUseCase(context: Context): ThemeStorageInteractor {
        return ThemeStorageInteractorImpl(getThemeStorageRepository(context))
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(mediaPlayer)
    }

    fun provideMediaPlayerUseCase(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(application))
    }
}
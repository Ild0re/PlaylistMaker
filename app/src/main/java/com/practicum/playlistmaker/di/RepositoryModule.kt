package com.practicum.playlistmaker.di

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
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksStorageRepository> {
        TracksStorageRepositoryImpl(get())
    }

    single<ThemeStorageRepository> {
        ThemeStorageRepositoryImpl(get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }
}
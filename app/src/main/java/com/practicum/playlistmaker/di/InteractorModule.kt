package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.favourites.FavouritesInteractor
import com.practicum.playlistmaker.domain.favourites.FavouritesInteractorImpl
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
import org.koin.dsl.module

val interactorModule = module {
    single<TracksStorageInteractor> {
        TracksStorageInteractorImpl(get())
    }

    single<ThemeStorageInteractor> {
        ThemeStorageInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    single<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }
}
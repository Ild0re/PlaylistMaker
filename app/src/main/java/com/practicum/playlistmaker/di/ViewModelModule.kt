package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.library.view_model.FavouritesViewModel
import com.practicum.playlistmaker.ui.library.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.ui.playlist.changePlaylist.view_model.ChangePlaylistViewModel
import com.practicum.playlistmaker.ui.playlist.createPlaylist.activity.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.ui.playlist.pickPlaylist.view_model.PickPlaylistViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.track.view_model.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel { (trackIntent: String) ->
        TrackViewModel(trackIntent, get(), get(), get())
    }

    viewModel {
        FavouritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel { (id: Int) ->
        PickPlaylistViewModel(id, get())
    }

    viewModel { (id: Int) ->
        ChangePlaylistViewModel(id, get())
    } 

}
package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator

class SearchViewModel: ViewModel() {
    private val getSearchInteractor = Creator.provideTracksSearchUseCase()


}
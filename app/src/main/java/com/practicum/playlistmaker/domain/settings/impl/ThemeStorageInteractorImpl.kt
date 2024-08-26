package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.data.settings.ThemeStorageRepository
import com.practicum.playlistmaker.domain.settings.interactor.ThemeStorageInteractor

class ThemeStorageInteractorImpl(private val repository: ThemeStorageRepository) :
    ThemeStorageInteractor {
    override fun getTheme(): String {
        return repository.getTheme()
    }

    override fun saveTheme(theme: Boolean): Boolean {
        return repository.saveTheme(theme)
    }
}
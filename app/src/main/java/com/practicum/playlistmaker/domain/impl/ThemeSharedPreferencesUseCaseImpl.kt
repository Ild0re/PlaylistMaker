package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.repository.ThemeSharedPreferencesRepository
import com.practicum.playlistmaker.domain.use_cases.ThemeSharedreferencesUseCase

class ThemeSharedPreferencesUseCaseImpl(private val repository: ThemeSharedPreferencesRepository) :
    ThemeSharedreferencesUseCase {
    override fun getTheme(): String {
        return repository.getTheme()
    }

    override fun saveTheme(theme: Boolean): Boolean {
        return repository.saveTheme(theme)
    }
}
package com.practicum.playlistmaker.domain.use_cases

interface ThemeSharedreferencesUseCase {

    fun getTheme(): String

    fun saveTheme(theme: Boolean): Boolean

}
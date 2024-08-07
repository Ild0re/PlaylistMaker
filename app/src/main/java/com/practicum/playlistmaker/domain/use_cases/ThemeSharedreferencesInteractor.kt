package com.practicum.playlistmaker.domain.use_cases

interface ThemeSharedreferencesInteractor {

    fun getTheme(): String

    fun saveTheme(theme: Boolean): Boolean

}
package com.practicum.playlistmaker.domain.settings.interactor

interface ThemeStorageInteractor {

    fun getTheme(): String

    fun saveTheme(theme: Boolean): Boolean

}
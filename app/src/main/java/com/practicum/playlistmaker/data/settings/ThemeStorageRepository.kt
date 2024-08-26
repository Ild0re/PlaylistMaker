package com.practicum.playlistmaker.data.settings

interface ThemeStorageRepository {

    fun getTheme(): String

    fun saveTheme(theme: Boolean): Boolean
}
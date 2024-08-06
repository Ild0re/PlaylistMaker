package com.practicum.playlistmaker.domain.repository

interface ThemeSharedPreferencesRepository {

    fun getTheme(): String

    fun saveTheme(theme: Boolean): Boolean
}
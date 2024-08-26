package com.practicum.playlistmaker.data.settings

import android.content.SharedPreferences


class ThemeStorageRepositoryImpl(private val sf: SharedPreferences) : ThemeStorageRepository {

    override fun getTheme(): String {
        val theme = sf.getString(DARK_THEME, "")
        return theme.toString()
    }

    override fun saveTheme(theme: Boolean): Boolean {
        sf.edit()
            .putString(DARK_THEME, theme.toString())
            .apply()

        return theme
    }

    companion object {
        private const val DARK_THEME = "dark_theme"
    }
}
package com.practicum.playlistmaker.data.settings

import android.content.SharedPreferences

const val PREFERENCES = "dark_mode_preferences"
const val DARK_THEME = "dark_theme"

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
}
package com.practicum.playlistmaker.data.settings

import android.content.Context
import com.practicum.playlistmaker.creator.Creator

const val PREFERENCES = "dark_mode_preferences"
const val DARK_THEME = "dark_theme"

class ThemeStorageRepositoryImpl : ThemeStorageRepository {

    val sharedPreferences = Creator.application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    override fun getTheme(): String {
        val theme = sharedPreferences.getString(DARK_THEME, "")
        return theme.toString()
    }

    override fun saveTheme(theme: Boolean): Boolean {
        sharedPreferences.edit()
            .putString(DARK_THEME, theme.toString())
            .apply()

        return theme
    }
}
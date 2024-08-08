package com.practicum.playlistmaker.data.repository

import android.content.Context
import com.practicum.playlistmaker.domain.repository.ThemeSharedPreferencesRepository

const val PREFERENCES = "dark_mode_preferences"
const val DARK_THEME = "dark_theme"

class ThemeSharedPreferencesRepositoryImpl(context: Context) : ThemeSharedPreferencesRepository {

    val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

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
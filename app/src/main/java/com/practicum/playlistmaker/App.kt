package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PREFERENCES = "dark_mode_preferences"
const val DARK_THEME = "dark_theme"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPref = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        var themeKey = sharedPref.getString(DARK_THEME, "")

        when(themeKey) {
            "true" -> darkTheme = true
            else -> darkTheme = false
        }

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPref = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPref.edit()
            .putString(DARK_THEME, darkTheme.toString())
            .apply()
    }
}
package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val getThemeSharedPreferencesUseCase = Creator.provideThemeStorageUseCase(this)
        var themeKey = getThemeSharedPreferencesUseCase.getTheme()

        when (themeKey) {
            "true" -> darkTheme = true
            else -> darkTheme = false
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val getThemeSharedPreferencesUseCase = Creator.provideThemeStorageUseCase(this)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        getThemeSharedPreferencesUseCase.saveTheme(darkTheme)
    }
}
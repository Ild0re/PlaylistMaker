package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val getThemeSharedPreferencesUseCase = Creator.provideThemeStorageUseCase()
        var themeKey = getThemeSharedPreferencesUseCase.getTheme()

        when (themeKey) {
            "true" -> darkTheme = true
            else -> darkTheme = false
        }
    }
}
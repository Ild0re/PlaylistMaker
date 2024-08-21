package com.practicum.playlistmaker.ui.settings.view_model

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel : ViewModel() {

    private val getSharingUseCase = Creator.provideSharingInteractor()

    fun onShareAppClick(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getSharingUseCase.shareApp())
        val chooser =
            Intent.createChooser(shareIntent, Creator.application.getString(R.string.ButtonShare))
        return chooser
    }

    fun onOpenTerms(): Intent {
        val licenseIntent = Intent(Intent.ACTION_VIEW)
        licenseIntent.data = Uri.parse(getSharingUseCase.openTerms())
        return licenseIntent
    }

    fun onOpenSupport(): Intent {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getSharingUseCase.openSupport().email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, getSharingUseCase.openSupport().subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, getSharingUseCase.openSupport().text)
        return supportIntent
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val darkTheme = darkThemeEnabled
        val getThemeSharedPreferencesUseCase = Creator.provideThemeStorageUseCase()
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
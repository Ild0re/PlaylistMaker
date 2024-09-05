package com.practicum.playlistmaker.ui.settings.view_model

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.interactor.ThemeStorageInteractor
import com.practicum.playlistmaker.domain.sharing.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeStorageInteractor: ThemeStorageInteractor,
) : ViewModel() {

    fun onShareAppClick(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharingInteractor.shareApp())
        val chooser =
            Intent.createChooser(shareIntent, "Выберите приложение для отправки")
        return chooser
    }

    fun onOpenTerms(): Intent {
        val licenseIntent = Intent(Intent.ACTION_VIEW)
        licenseIntent.data = Uri.parse(sharingInteractor.openTerms())
        return licenseIntent
    }

    fun onOpenSupport(): Intent {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(sharingInteractor.openSupport().email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, sharingInteractor.openSupport().subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, sharingInteractor.openSupport().text)
        return supportIntent
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themeStorageInteractor.saveTheme(darkThemeEnabled)
    }

    fun getTheme(): Boolean {
        return themeStorageInteractor.getTheme().toBoolean()
    }
}
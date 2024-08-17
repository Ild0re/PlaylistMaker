package com.practicum.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val getSharingUseCase = Creator.provideSharingInteractor(application)

    fun onShareAppClick(): Intent {
        return getSharingUseCase.shareApp()
    }

    fun onOpenTerms(): Intent {
        return getSharingUseCase.openTerms()
    }

    fun onOpenSupport(): Intent {
        return getSharingUseCase.openSupport()
    }

}
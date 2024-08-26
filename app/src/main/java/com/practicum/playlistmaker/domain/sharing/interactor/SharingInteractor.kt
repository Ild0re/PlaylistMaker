package com.practicum.playlistmaker.domain.sharing.interactor

import android.content.Intent
import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface SharingInteractor {
    fun shareApp(): String
    fun openTerms(): String
    fun openSupport(): EmailData
}
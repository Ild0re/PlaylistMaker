package com.practicum.playlistmaker.domain.sharing.interactor

import android.content.Intent

interface SharingInteractor {
    fun shareApp(): Intent
    fun openTerms(): Intent
    fun openSupport(): Intent
}
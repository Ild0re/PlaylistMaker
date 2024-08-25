package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(): String
    fun openLink(): String
    fun openEmail(): EmailData
}
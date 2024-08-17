package com.practicum.playlistmaker.data.sharing

import android.content.Intent

interface ExternalNavigator {
    fun shareLink(): Intent
    fun openLink(): Intent
    fun openEmail(): Intent
}
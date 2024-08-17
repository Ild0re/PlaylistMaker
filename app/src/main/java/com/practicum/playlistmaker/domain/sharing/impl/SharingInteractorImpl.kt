package com.practicum.playlistmaker.domain.sharing.impl

import android.content.Intent
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(): Intent {
        return externalNavigator.shareLink()
    }

    override fun openTerms(): Intent {
        return externalNavigator.openLink()
    }

    override fun openSupport(): Intent {
        return externalNavigator.openEmail()
    }

}
package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.interactor.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(): String {
        return externalNavigator.shareLink()
    }

    override fun openTerms(): String {
        return externalNavigator.openLink()
    }

    override fun openSupport(): EmailData {
        return externalNavigator.openEmail()
    }

}
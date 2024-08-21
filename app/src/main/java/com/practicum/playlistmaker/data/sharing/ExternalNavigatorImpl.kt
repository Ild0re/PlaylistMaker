package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl : ExternalNavigator {

    override fun shareLink(): String {
        val appLink = Creator.application.getString(R.string.linkShare)
        return appLink
    }

    override fun openLink(): String {
        val linkLicense = Creator.application.getString(R.string.linkLicense)
        return linkLicense
    }

    override fun openEmail(): EmailData  {
        val test_email = EmailData(
            "nuruda@yandex.ru",
            Creator.application.getString(R.string.buttonSupport),
            Creator.application.getString(R.string.mail_body_message)
        )
        return test_email
    }
}
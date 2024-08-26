package com.practicum.playlistmaker.data.sharing

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(): String {
        val appLink = context.getString(R.string.linkShare)
        return appLink
    }

    override fun openLink(): String {
        val linkLicense = context.getString(R.string.linkLicense)
        return linkLicense
    }

    override fun openEmail(): EmailData {
        val test_email = EmailData(
            "nuruda@yandex.ru",
            context.getString(R.string.buttonSupport),
            context.getString(R.string.mail_body_message)
        )
        return test_email
    }
}
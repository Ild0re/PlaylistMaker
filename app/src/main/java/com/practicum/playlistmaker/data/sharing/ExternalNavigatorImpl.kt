package com.practicum.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.linkShare))
        val chooser = Intent.createChooser(shareIntent, context.getString(R.string.ButtonShare))
        return chooser
    }

    override fun openLink(): Intent {
        val licenseIntent = Intent(Intent.ACTION_VIEW)
        licenseIntent.data = Uri.parse(context.getString(R.string.linkLicense))
        return licenseIntent
    }

    override fun openEmail(): Intent  {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        val test_email = EmailData(
            "nuruda@yandex.ru",
            context.getString(R.string.buttonSupport),
            context.getString(R.string.mail_body_message)
        )
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(test_email.email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, test_email.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, test_email.text)
        return supportIntent
    }
}
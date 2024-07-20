package com.practicum.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        val buttonShare = findViewById<ImageButton>(R.id.buttonShare)
        val buttonSupport = findViewById<ImageButton>(R.id.buttonSupport)
        val buttonUserDoc = findViewById<ImageButton>(R.id.userDoc)
        val switchDarkTheme = findViewById<SwitchMaterial>(R.id.themeSwitcher)




        switchDarkTheme.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        switchDarkTheme.isChecked = (applicationContext as App).darkTheme




        buttonBack.setOnClickListener {
            finish()
        }
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkShare))
            val chooser = Intent.createChooser(shareIntent, getString(R.string.ButtonShare))
            startActivity(chooser)
        }
        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("nuruda@yandex.ru"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.buttonSupport))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_body_message))
            startActivity(supportIntent)
        }
        buttonUserDoc.setOnClickListener {
            val licenseIntent = Intent(Intent.ACTION_VIEW)
            licenseIntent.data = Uri.parse(getString(R.string.linkLicense))
            startActivity(licenseIntent)
        }
    }
}
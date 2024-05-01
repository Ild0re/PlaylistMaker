package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        val buttonShare = findViewById<ImageButton>(R.id.buttonShare)
        val buttonSupport = findViewById<ImageButton>(R.id.buttonSupport)
        val buttonUserDoc = findViewById<ImageButton>(R.id.userDoc)
        val switchDarkTheme = findViewById<Switch>(R.id.switchButton)
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveSwitchState(this@SettingsActivity, true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveSwitchState(this@SettingsActivity, false)
            }
        }

        val switchState = getSwitchState(this)
        switchDarkTheme.isChecked = switchState

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

    private fun saveSwitchState(context: Context, switchState: Boolean) {
        val sharedPreferences = context.getSharedPreferences("my_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("switch_state", switchState)
        editor.apply()
    }

    private fun getSwitchState(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("my_preferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("switch_state", false)
    }
}
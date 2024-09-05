package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        binding.themeSwitcher.isChecked = viewModel.getTheme()

        binding.buttonBackToMenu.setOnClickListener {
            finish()
        }

        binding.buttonShare.setOnClickListener {
            startActivity(viewModel.onShareAppClick())
        }

        binding.buttonSupport.setOnClickListener {
            startActivity(viewModel.onOpenSupport())
        }

        binding.userDoc.setOnClickListener {
            startActivity(viewModel.onOpenTerms())
        }
    }
}
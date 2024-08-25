package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.factory()
        )[SettingsViewModel::class.java]


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
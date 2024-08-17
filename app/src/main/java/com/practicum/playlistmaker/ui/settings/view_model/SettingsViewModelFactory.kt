package com.practicum.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator

//class SettingsViewModelFactory(context: Context): ViewModelProvider.Factory {
//
//    lateinit var app: Application
//
//    private val sharingInteractor by lazy(LazyThreadSafetyMode.NONE) {
//        Creator.provideSharingInteractor()
//    }
//
//    private val settingsInteractor by lazy(LazyThreadSafetyMode.NONE) {
//        Creator.provideThemeStorageUseCase(context)
//    }
//
//    override fun <T: ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return SettingsViewModel() as T
//        }
//        throw IllegalArgumentException("Unable to construct viewmodel")
//    }
//}
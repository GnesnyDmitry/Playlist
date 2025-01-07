package com.example.playlistmaker.di

import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.ThemeSwitcher
import com.example.playlistmaker.setting.data.ThemeSwitcherImpl
import com.example.playlistmaker.setting.domain.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import com.example.playlistmaker.setting.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            interactor = get()
        )
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            settingsRepository = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            themeSwitcher = get()
        )
    }

    factory<ThemeSwitcher> {
        ThemeSwitcherImpl(
            sharedPreferences = get()
        )
    }
}
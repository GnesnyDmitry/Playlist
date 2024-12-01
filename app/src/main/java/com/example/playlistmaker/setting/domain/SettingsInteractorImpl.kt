package com.example.playlistmaker.setting.domain

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getCurrentThemeState(): Boolean {
        return settingsRepository.isDarkThemeEnable()
    }

    override fun updateThemeState(isDarkTheme: Boolean) {
        settingsRepository.setDarkTheme(isDarkTheme)
    }

    override fun setThemeForApp() {
        when(settingsRepository.isDarkThemeEnable()) {
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}
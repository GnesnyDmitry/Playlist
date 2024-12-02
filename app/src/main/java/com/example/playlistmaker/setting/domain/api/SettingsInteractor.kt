package com.example.playlistmaker.setting.domain.api

interface SettingsInteractor {
    fun getCurrentThemeState(): Boolean
    fun updateThemeState(isDarkTheme: Boolean)
}
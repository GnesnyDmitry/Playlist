package com.example.playlistmaker.setting.domain.api

interface SettingsRepository {
    fun isDarkThemeEnable(): Boolean
    fun setDarkTheme(enable: Boolean)
}
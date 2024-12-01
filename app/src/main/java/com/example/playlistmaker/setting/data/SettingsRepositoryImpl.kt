package com.example.playlistmaker.setting.data

import android.content.SharedPreferences
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import com.example.playlistmaker.tools.THEME_KEY

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {

    override fun isDarkThemeEnable(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun setDarkTheme(enable: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, enable).apply()
    }

}
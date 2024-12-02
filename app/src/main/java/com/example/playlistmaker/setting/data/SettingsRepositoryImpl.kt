package com.example.playlistmaker.setting.data

import android.content.SharedPreferences
import android.content.res.Resources.Theme
import com.example.playlistmaker.App
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import com.example.playlistmaker.tools.THEME_KEY

class SettingsRepositoryImpl() : SettingsRepository {

    private val themeSwitcher = App.instance.themeSwitcher

    override fun isDarkThemeEnable(): Boolean {
        return themeSwitcher.getTheme()
    }

    override fun setDarkTheme(enable: Boolean) {
        themeSwitcher.setTheme(enable)
    }

}
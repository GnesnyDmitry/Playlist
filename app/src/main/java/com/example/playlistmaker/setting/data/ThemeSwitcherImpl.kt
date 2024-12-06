package com.example.playlistmaker.setting.data

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.App
import com.example.playlistmaker.tools.THEME_KEY

class ThemeSwitcherImpl(
    private val sharedPreferences: SharedPreferences
) : ThemeSwitcher {

    init {
        switchTheme(getTheme())
    }

    override fun getTheme(): Boolean =
        sharedPreferences.getBoolean(THEME_KEY, false)

    override fun setTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, isDarkTheme).apply()
        switchTheme(isDarkTheme)
    }

    private fun switchTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
package com.example.playlistmaker.setting.data

interface ThemeSwitcher {
    fun getTheme(): Boolean
    fun setTheme(isDarkTheme: Boolean)
}
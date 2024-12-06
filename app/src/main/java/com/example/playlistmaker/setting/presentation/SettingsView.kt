package com.example.playlistmaker.setting.presentation

interface SettingsView {
    fun onThemeSwitchChanged(): Boolean
    fun setThemeSwitcher(isChecked: Boolean)
}
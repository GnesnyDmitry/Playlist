package com.example.playlistmaker.setting.ui

import androidx.appcompat.app.AppCompatActivity

class SettingsRouter(private val view: AppCompatActivity) {
    fun goBack() { view.finish() }
}
package com.example.playlistmaker.setting.ui

import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.setting.presentation.SettingsView

class SettingsRouter(private val view: AppCompatActivity) {
    fun goBack() { view.finish() }
}
package com.example.playlistmaker.setting.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    private val themeState = MutableStateFlow<Boolean>(value = false)
    val themeStateFlow: StateFlow<Boolean> = themeState

    init {
        themeState.value = interactor.getCurrentThemeState()
    }

    fun changeTheme(theme: Boolean) {
        if (theme == themeState.value) return
        interactor.updateThemeState(theme)
        themeState.value = theme
    }
}
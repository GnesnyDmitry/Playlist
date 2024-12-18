package com.example.playlistmaker.setting.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.tools.SingleLiveEvent

class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    private val themeState = SingleLiveEvent<Boolean>()

    fun themeStateLiveData(): LiveData<Boolean> = themeState

    init {
        themeState.value = interactor.getCurrentThemeState()
    }

    fun changeTheme(theme: Boolean) {
        if (theme == themeState.value) return
        interactor.updateThemeState(theme)
        themeState.postValue(theme)
    }
}
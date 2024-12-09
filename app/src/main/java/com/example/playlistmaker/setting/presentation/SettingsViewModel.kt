package com.example.playlistmaker.setting.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
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

    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(interactor = Creator.provideSettingsInteractor())
                }
            }
    }
}
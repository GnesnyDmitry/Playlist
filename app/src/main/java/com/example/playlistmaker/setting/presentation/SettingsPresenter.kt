package com.example.playlistmaker.setting.presentation

import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.ui.SettingsRouter

class SettingsPresenter(
    private val view: SettingsView,
    private val router: SettingsRouter,
    private val interactor: SettingsInteractor
) {
    fun setThemeSwitcher() {
        view.setThemeSwitcher(interactor.getCurrentThemeState())
    }

    fun setNewSwitcherTheme() {
        interactor.updateThemeState(view.onThemeSwitchChanged())
        view.setThemeSwitcher(interactor.getCurrentThemeState())
    }

    fun onClickedBack() {
        router.goBack()
    }
}


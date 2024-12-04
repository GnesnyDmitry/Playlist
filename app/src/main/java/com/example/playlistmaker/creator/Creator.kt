package com.example.playlistmaker.creator

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.presentation.PlayerPresenter
import com.example.playlistmaker.player.ui.PlayerRouter
import com.example.playlistmaker.player.presentation.PlayerView
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.DtoTrackMapper
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.search.presentation.SearchPresenter
import com.example.playlistmaker.search.ui.SearchRouter
import com.example.playlistmaker.search.presentation.SearchView
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.ThemeSwitcher
import com.example.playlistmaker.setting.data.ThemeSwitcherImpl
import com.example.playlistmaker.setting.domain.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import com.example.playlistmaker.setting.presentation.SettingsPresenter
import com.example.playlistmaker.setting.ui.SettingsRouter
import com.example.playlistmaker.setting.presentation.SettingsView

object Creator {

    private const val SHARED_PREFERENCE_THEME = "tracks_preferences"

    private val sharedPreferenceTheme = App.instance.getSharedPreferences(SHARED_PREFERENCE_THEME, MODE_PRIVATE)

    private val themeSwitcher = ThemeSwitcherImpl(sharedPreferenceTheme)

    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(), DtoTrackMapper())
    }

    private fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    fun createSearchPresenter(searchView: SearchView, router: SearchRouter): SearchPresenter {
        return SearchPresenter(
            view = searchView,
            router = router,
            searchInteractor = provideTracksInteractor()
        )
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun createPlayerPresenter(view: PlayerView, router: PlayerRouter): PlayerPresenter {
        return PlayerPresenter(
            view = view,
            router = router,
            interactor = providePlayerInteractor()
        )
    }

    private fun getThemeSwitcher(): ThemeSwitcher {
        return ThemeSwitcherImpl(sharedPreferenceTheme)
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getThemeSwitcher())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun createSettingsPresenter(view: SettingsView, router: SettingsRouter): SettingsPresenter {
        return SettingsPresenter(
            view = view,
            router = router,
            interactor = provideSettingsInteractor()
        )
    }
}
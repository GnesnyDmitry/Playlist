package com.example.playlistmaker.creator

import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.App
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.DtoTrackMapper
import com.example.playlistmaker.search.data.GsonConverter
import com.example.playlistmaker.search.data.LocalTrackStorage
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.ThemeSwitcher
import com.example.playlistmaker.setting.data.ThemeSwitcherImpl
import com.example.playlistmaker.setting.domain.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository

object Creator {

    private const val APP_PREFERENCES = "app_preference"

    private val sharedPreference = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

    private fun getGsonConverter(): GsonConverter {
        return GsonConverter(App.instance.gson)
    }

    private fun getLocalTrackStorage(): LocalTrackStorage {
        return LocalTrackStorage(getGsonConverter(), sharedPreference)
    }

    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(), DtoTrackMapper(), getLocalTrackStorage())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getThemeSwitcher(): ThemeSwitcher {
        return ThemeSwitcherImpl(sharedPreference)
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getThemeSwitcher())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

}
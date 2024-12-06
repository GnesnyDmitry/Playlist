package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.google.gson.Gson


class App : Application() {

    internal lateinit var trackStorage: TrackStorage
    internal val gson = Gson()
    internal lateinit var themeSwitcher: SettingsInteractor


    override fun onCreate() {
        super.onCreate()

        instance = this
        trackStorage =
            TrackStorage(getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE), gson)

        themeSwitcher = Creator.provideSettingsInteractor()
    }

    companion object {
        const val TRACKS_PREFERENCES = "tracks_preferences"
        lateinit var instance: App
    }
}
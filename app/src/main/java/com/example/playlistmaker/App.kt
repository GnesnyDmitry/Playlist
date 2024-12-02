package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.setting.data.ThemeSwitcher
import com.example.playlistmaker.setting.data.ThemeSwitcherImpl
import com.google.gson.Gson


class App : Application() {

    internal lateinit var trackStorage: TrackStorage
    internal val gson = Gson()
    internal lateinit var themeSwitcher: ThemeSwitcher


    override fun onCreate() {
        super.onCreate()

        instance = this
        trackStorage =
            TrackStorage(getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE), gson)

        themeSwitcher = ThemeSwitcherImpl(getSharedPreferences(SHARED_PREFERENCE_THEME, MODE_PRIVATE))
    }

    companion object {
        const val TRACKS_PREFERENCES = "tracks_preferences"
        const val SHARED_PREFERENCE_THEME = "tracks_preferences"
        lateinit var instance: App
    }
}
package com.example.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.preferenceStorage.TrackStorage
import com.google.gson.Gson

class App : Application() {

    internal lateinit var trackStorage: TrackStorage
    internal val gson = Gson()

    override fun onCreate() {
        super.onCreate()

        instance = this
        trackStorage =
            TrackStorage(getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE), gson)

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCE_THEME, MODE_PRIVATE)

        switchTheme(sharedPrefs.getBoolean("THEME_STATE", false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object {
        const val TRACKS_PREFERENCES = "tracks_preferences"
        lateinit var instance: App
    }
}